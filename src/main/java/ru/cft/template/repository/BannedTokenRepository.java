package ru.cft.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.entity.BannedToken;

import java.util.Optional;

@Repository
public interface BannedTokenRepository extends JpaRepository<BannedToken, String> {
    Optional<BannedToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO banned_tokens (token) VALUES (:token) ON CONFLICT (token) DO NOTHING", nativeQuery = true)
    void insertIfNotExist(BannedToken bannedToken);
}
