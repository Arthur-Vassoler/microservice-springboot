package br.com.microlog.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRecordDto(@NotBlank String name, @NotBlank @Email String email) {
}
