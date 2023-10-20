package br.com.microlog.user.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ResetPasswordDto(@NotBlank @Length(min = 3) String password, @NotBlank String token) {
}
