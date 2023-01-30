package com.formmaker.fff.gift.dto.request;

import lombok.Getter;

@Getter
public class GiftCreateRequest {
    private String giftName;
    private String giftSummary;
    private String giftIcon;
    private Integer giftQuantity;
}
