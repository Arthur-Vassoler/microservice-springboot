package br.com.microlog.email.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_NEW_USER_EMAILS")
@Data
public class NewUserEmailModel extends GenericEmailModel {
}
