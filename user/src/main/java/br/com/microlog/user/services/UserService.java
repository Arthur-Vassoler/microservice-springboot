package br.com.microlog.user.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.microlog.user.models.UserModel;
import br.com.microlog.user.producers.UserProducer;
import br.com.microlog.user.repositories.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  final UserRepository userRepository;
  final UserProducer userProducer;

  public UserService(UserRepository userRepository, UserProducer userProducer) {
    this.userRepository = userRepository;
    this.userProducer = userProducer;
  }

  @Transactional
  public UserModel save(UserModel userModel) {
    if (userRepository.existsByEmail(userModel.getEmail())) {
      throw new DuplicateKeyException("Email já cadastrado.");
    }

    var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
    userModel.setPassword(passwordHash);
    userModel = userRepository.save(userModel);

    userProducer.publishMessageEmail(userModel);

    return userModel;
  }

  public UserModel findByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
  }

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
  }
}
