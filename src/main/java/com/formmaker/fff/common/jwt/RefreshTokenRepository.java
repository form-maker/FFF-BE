package com.formmaker.fff.common.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    boolean existsByKeyLoginId(String loginId);
    void deleteByKeyLoginId(String loginId);

}
