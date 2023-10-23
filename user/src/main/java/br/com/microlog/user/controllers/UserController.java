package br.com.microlog.user.controllers;

import br.com.microlog.user.dtos.CodeRecoverySuccessfullyEmailDto;
import br.com.microlog.user.dtos.ResetPasswordDto;
import br.com.microlog.user.dtos.UserUpdateRecordDto;
import br.com.microlog.user.models.UserModel;
import br.com.microlog.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
  final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<?> findByEmail(@PathVariable String email) {
    try {
      return new ResponseEntity<>(userService.findByUsername(email), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/id/{userId}")
  public ResponseEntity<?> findById(@PathVariable UUID userId) {
    try {
      return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/id/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody @Valid UserUpdateRecordDto userRecordDto) {
    UserModel existingUser = userService.findById(userId);

    if (existingUser == null)
      return ResponseEntity.notFound().build();

    if (!existingUser.getEmail().equals(userRecordDto.email()))
      return ResponseEntity.badRequest().body("Este email já está em uso.");

    BeanUtils.copyProperties(userRecordDto, existingUser, "password");

    return ResponseEntity.ok(userService.save(existingUser));
  }

  @PostMapping("/reset-password/{userId}")
  public ResponseEntity<?> resetPassword(@PathVariable UUID userId, @RequestBody @Valid ResetPasswordDto resetPasswordDto) {
    try {
      userService.updatePassword(userId, resetPasswordDto);
      return new ResponseEntity<>("Senha atualizada com sucesso!", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/id/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody CodeRecoverySuccessfullyEmailDto codeRecoverySuccessfullyEmailDto) {
    try {
      userService.delete(userId, codeRecoverySuccessfullyEmailDto);
      return new ResponseEntity<>("Usuário deletado com sucesso!", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/token-delete/{userId}")
  public ResponseEntity<?> requestTokenForDelete(@PathVariable UUID userId) {
    try {
      userService.sendTokenForDeleteRequest(userId);
      return new ResponseEntity<>("Token enviado verifique sua caixa de email.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/token-password/{userId}")
  public ResponseEntity<?> requestTokenForPasswordReset(@PathVariable UUID userId) {
    try {
      userService.sendTokenForPasswordResetRequest(userId);
      return new ResponseEntity<>("Token enviado verifique sua caixa de email.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
