package br.com.microlog.email.dtos;

import br.com.microlog.email.enums.TypeCodeRecoveryEnum;

import java.util.UUID;

public record CodeRecoveryEmailRecordDto(
  UUID userId,
  String emailTo,
  String username,
  TypeCodeRecoveryEnum typeCodeRecovery,
  String token) {
}
