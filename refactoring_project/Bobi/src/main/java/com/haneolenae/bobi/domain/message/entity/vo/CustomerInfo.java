package com.haneolenae.bobi.domain.message.entity.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class CustomerInfo {
    private final String name;
    private final String phoneNumber;
    private final String color;
}
