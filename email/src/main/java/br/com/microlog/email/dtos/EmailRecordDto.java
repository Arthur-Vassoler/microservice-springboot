package br.com.microlog.email.dtos;

import java.util.UUID;

public record EmailRecordDto(
  UUID userId,
  String emailTo,
  String username) {
}
