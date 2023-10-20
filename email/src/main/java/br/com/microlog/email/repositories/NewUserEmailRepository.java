package br.com.microlog.email.repositories;

import br.com.microlog.email.models.NewUserEmailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewUserEmailRepository extends JpaRepository<NewUserEmailModel, UUID> {
}
