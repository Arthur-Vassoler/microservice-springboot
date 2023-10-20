package br.com.microlog.email.repositories;

import br.com.microlog.email.models.CodeRecoveryEmailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CodeRecoveryEmailRepository extends JpaRepository<CodeRecoveryEmailModel, UUID> {
}
