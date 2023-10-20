package br.com.microlog.user.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_USER_TOKEN_FOR_PRIVATE_REQUESTS")
@Data
public class UserTokenForPrivateRequests implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private UUID userId;

  private String token;
}
