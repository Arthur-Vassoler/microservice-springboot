package br.com.microlog.user.controllers;

import br.com.microlog.user.communs.JwtUtils;
import br.com.microlog.user.dtos.LoginRecordDto;
import br.com.microlog.user.dtos.UserRecordDto;
import br.com.microlog.user.models.UserModel;
import br.com.microlog.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final UserService userService;

  public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<UserModel> save(@RequestBody @Valid UserRecordDto userRecordDto) {
    var userModel = new UserModel();
    BeanUtils.copyProperties(userRecordDto, userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody @Valid LoginRecordDto login) {
    Authentication authentication =
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(login.email(), login.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String tokenJwt = jwtUtils.generateTokenFromUsername(login.email());

    return ResponseEntity.ok().body(tokenJwt);
  }
}