package com.team11.user_service.domain.repository;

import com.team11.user_service.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndIsDeletedFalse(Long userId);

    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    Optional<User> findByUsernameAndIsDeletedFalse(String username);
}