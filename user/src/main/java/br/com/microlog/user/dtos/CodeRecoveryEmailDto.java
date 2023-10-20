package br.com.microlog.user.dtos;

import br.com.microlog.user.enums.TypeCodeRecoveryEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class CodeRecoveryEmailDto {
  private UUID userId;
  private String emailTo;
  private String username;
  private TypeCodeRecoveryEnum typeCodeRecovery;
  private String Token;
}
