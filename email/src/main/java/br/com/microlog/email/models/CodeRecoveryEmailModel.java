package br.com.microlog.email.models;

import br.com.microlog.email.enums.TypeCodeRecoveryEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TB_CODE_RECOVERY_EMAILS")
@Data
public class CodeRecoveryEmailModel extends GenericEmailModel {
  private TypeCodeRecoveryEnum typeCodeRecovery;
  private String token;
}
