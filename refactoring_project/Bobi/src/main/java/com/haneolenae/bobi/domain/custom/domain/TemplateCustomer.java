package com.haneolenae.bobi.domain.custom.domain;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.CustomTemplateEntity;
import com.haneolenae.bobi.domain.customer.entity.Customer;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TemplateCustomer {
    private final Long id;

    private final CustomTemplate customTemplate;

    //수정 필요
    private final Customer customer;

}
