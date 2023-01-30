package com.formmaker.fff.gift.giftRepository;

import com.formmaker.fff.gift.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftRepository extends JpaRepository<Gift, Long> {
}
