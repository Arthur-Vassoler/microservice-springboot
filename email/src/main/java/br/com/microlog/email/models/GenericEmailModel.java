package br.com.microlog.email.models;

import br.com.microlog.email.enums.StatusEmail;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class GenericEmailModel extends RepresentationModel<GenericEmailModel> implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID emailId;
  private UUID userId;
  private String username;
  private String emailFrom;
  private String emailTo;
  private String subject;

  @Column(columnDefinition = "TEXT")
  private String text;

  private LocalDateTime sendDateEmail;
  private StatusEmail statusEmail;
}
