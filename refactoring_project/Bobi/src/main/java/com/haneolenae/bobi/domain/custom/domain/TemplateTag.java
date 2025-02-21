package com.haneolenae.bobi.domain.custom.domain;

import com.haneolenae.bobi.domain.tag.entity.Tag;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TemplateTag {
    private final Long id;

    private final CustomTemplate customTemplate;

    //수정 필요
    private Tag tag;
}
