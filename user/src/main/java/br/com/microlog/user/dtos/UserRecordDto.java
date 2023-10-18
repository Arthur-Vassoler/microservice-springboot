package br.com.microlog.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserRecordDto(
  @NotBlank String name,
  @NotBlank @Email String email,
  @NotBlank @Length(min = 3) String password ){
}
