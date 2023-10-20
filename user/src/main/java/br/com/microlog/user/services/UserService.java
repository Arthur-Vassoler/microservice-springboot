package br.com.microlog.user.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.microlog.user.dtos.CodeRecoverySuccessfullyEmailDto;
import br.com.microlog.user.dtos.ResetPasswordDto;
import br.com.microlog.user.enums.TypeCodeRecoveryEnum;
import br.com.microlog.user.models.UserTokenForPrivateRequests;
import br.com.microlog.user.models.UserModel;
import br.com.microlog.user.producers.UserProducer;
import br.com.microlog.user.repositories.UserRepository;
import br.com.microlog.user.repositories.UserTokenFroPrivateRequests;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
  private static final int STRING_LENGTH = 6;

  final UserRepository userRepository;
  final UserTokenFroPrivateRequests userTokenFroPrivateRequests;
  final UserProducer userProducer;

  public UserService(UserRepository userRepository, UserTokenFroPrivateRequests userTokenFroPrivateRequests, UserProducer userProducer) {
    this.userRepository = userRepository;
    this.userTokenFroPrivateRequests = userTokenFroPrivateRequests;
    this.userProducer = userProducer;
  }

  @Transactional
  public UserModel save(UserModel userModel) {
    if (userRepository.existsByEmail(userModel.getEmail())) {
      throw new DuplicateKeyException("Email já cadastrado.");
    }

    boolean isUpdate = userModel.getUserId() != null;

    if (!isUpdate) {
      var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
      userModel.setPassword(passwordHash);
    }

    userModel = userRepository.save(userModel);
    userProducer.publishNewUserMessageEmail(userModel);

    return userModel;
  }

  @Transactional
  public void updatePassword(UUID userId, ResetPasswordDto resetPasswordDto) {
    var userModel = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

    var userToken = userTokenFroPrivateRequests.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Token não encontrado."));
    var isTokenValid = BCrypt.verifyer().verify(resetPasswordDto.token().toCharArray(), userToken.getToken()).verified;

    if (!isTokenValid)
      throw new IllegalArgumentException("Token inválido.");

    var passwordHash = BCrypt.withDefaults().hashToString(12, resetPasswordDto.password().toCharArray());
    userModel.setPassword(passwordHash);

    userRepository.save(userModel);
    userTokenFroPrivateRequests.delete(userToken);

    if (userRepository.existsById(userId) && !userTokenFroPrivateRequests.existsByUserId(userId)) {
      userProducer.publishCodeRecoverySuccessfullyMessageEmail(userModel, TypeCodeRecoveryEnum.USERNAME_PASSWORD_CHANGE);
    }
  }

  public UserModel findByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
  }

  public UserModel findById(UUID userId) {
    return userRepository.findById(userId).orElse(null);
  }

  @Transactional
  public void delete(UUID uuid, CodeRecoverySuccessfullyEmailDto codeRecoverySuccessfullyEmailDto) {
    var user = userRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

    var userToken = userTokenFroPrivateRequests.findByUserId(uuid).orElseThrow(() -> new EntityNotFoundException("Token não encontrado."));
    var isTokenValid = BCrypt.verifyer().verify(codeRecoverySuccessfullyEmailDto.getToken().toCharArray(), userToken.getToken()).verified;

    if (!isTokenValid)
      throw new IllegalArgumentException("Token inválido.");

    userRepository.delete(user);
    userTokenFroPrivateRequests.delete(userToken);

    if (!userRepository.existsById(uuid) && !userTokenFroPrivateRequests.existsByUserId(uuid)) {
      userProducer.publishCodeRecoverySuccessfullyMessageEmail(user, TypeCodeRecoveryEnum.USERNAME_DELETE);
    }
  }

  private void userTokenForPrivateRequests(UUID userId, TypeCodeRecoveryEnum typeCodeRecoveryEnum) {
    var userModel = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

    var code = generateRandomString();
    var userToken = new UserTokenForPrivateRequests();
    userToken.setUserId(userModel.getUserId());
    userToken.setToken(BCrypt.withDefaults().hashToString(12, code.toCharArray()));

    userTokenFroPrivateRequests.save(userToken);
    userProducer.publishCodeRecoveryMessageEmail(userModel, code, typeCodeRecoveryEnum);
  }

  public void sendTokenForDeleteRequest(UUID userId) {
    var userModel = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    userTokenForPrivateRequests(userModel.getUserId(), TypeCodeRecoveryEnum.USERNAME_DELETE);
  }

  public void sendTokenForPasswordResetRequest(UUID userId) {
    var userModel = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    userTokenForPrivateRequests(userModel.getUserId(), TypeCodeRecoveryEnum.USERNAME_PASSWORD_CHANGE);
  }

  private static String generateRandomString() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random();
    StringBuilder sb = new StringBuilder(UserService.STRING_LENGTH);

    for (int i = 0; i < UserService.STRING_LENGTH; i++) {
      int randomIndex = random.nextInt(characters.length());
      char randomChar = characters.charAt(randomIndex);
      sb.append(randomChar);
    }

    return sb.toString();
  }
}
