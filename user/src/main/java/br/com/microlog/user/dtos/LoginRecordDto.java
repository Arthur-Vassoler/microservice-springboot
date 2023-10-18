package br.com.microlog.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRecordDto(
  @NotBlank @Email String email,
  @NotBlank String password
) {
}
