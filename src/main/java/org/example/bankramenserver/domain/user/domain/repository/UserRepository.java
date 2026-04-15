package org.example.bankramenserver.domain.user.domain.repository;

import org.example.bankramenserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
