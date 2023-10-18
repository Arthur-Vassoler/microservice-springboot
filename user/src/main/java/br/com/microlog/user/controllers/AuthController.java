package br.com.microlog.user.controllers;

import br.com.microlog.user.communs.JwtUtils;
import br.com.microlog.user.dtos.LoginRecordDto;
import br.com.microlog.user.dtos.LoginResponseDTO;
import br.com.microlog.user.dtos.UserRecordDto;
import br.com.microlog.user.models.UserModel;
import br.com.microlog.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtUtils jwtUtils;

  public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/signup")
  public ResponseEntity<UserModel> save(@RequestBody @Valid UserRecordDto userRecordDto) {
    var userModel = new UserModel();
    BeanUtils.copyProperties(userRecordDto, userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody @Valid LoginRecordDto login) throws InvalidKeyException {
    var usernamePassword = new UsernamePasswordAuthenticationToken(login.email(), login.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = jwtUtils.generateTokenFromUsername(auth.getName());

    return ResponseEntity.ok(new LoginResponseDTO(token));
  }
}