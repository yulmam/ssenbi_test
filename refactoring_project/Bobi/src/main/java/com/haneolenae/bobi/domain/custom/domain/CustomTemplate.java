package com.haneolenae.bobi.domain.custom.domain;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.TemplateCustomerEntity;
import com.haneolenae.bobi.domain.custom.infrastructure.entity.TemplateTagEntity;
import com.haneolenae.bobi.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
public class CustomTemplate {
    private final Long id;

    private final String title;

    private final String content;

    private final Integer count;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final List<TemplateCustomerEntity> templateCustomers;

    private final List<TemplateTag> templateTags;

    //수정 필요
    private final Member member;
}
