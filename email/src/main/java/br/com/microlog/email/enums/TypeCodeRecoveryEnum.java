package br.com.microlog.email.enums;

import lombok.Getter;

@Getter
public enum TypeCodeRecoveryEnum {
  USERNAME_PASSWORD_CHANGE(0, "change user password"),
  USERNAME_DELETE(1, "user delete");

  private final int code;
  private final String description;

  TypeCodeRecoveryEnum(int code, String description) {
    this.code = code;
    this.description = description;
  }
}
