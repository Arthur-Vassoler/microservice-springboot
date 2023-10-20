package br.com.microlog.user.repositories;

import br.com.microlog.user.models.UserTokenForPrivateRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTokenFroPrivateRequests extends JpaRepository<UserTokenForPrivateRequests, UUID> {
  Optional<UserTokenForPrivateRequests> findByUserId(UUID userId);

  boolean existsByUserId(UUID userId);
}
