package com.formmaker.fff.gift.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GiftResponse {
    private String giftName;
    private String giftSummary;
    private String giftIcon;
    private Integer giftQuantity;
}
