package com.haneolenae.bobi.domain.custom.infrastructure;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.TemplateCustomerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TemplateCustomerJPARepository  extends CrudRepository<TemplateCustomerEntity, Long> {
    List<TemplateCustomerEntity> findByCustomerIdIn(List<Long> customerIds);

    @Modifying
    @Query("DELETE FROM TemplateCustomerEntity t WHERE t.customTemplate.id = :customTemplateId")
    void deleteByCustomTemplateId(Long customTemplateId);

    void deleteByCustomerId(Long customerId);

    void deleteByCustomerIdAndCustomTemplateId(Long customerId, Long customTemplateId);

    @Query("SELECT t.customer.id FROM TemplateCustomerEntity t WHERE t.customTemplate.id = :templateId")
    Set<Long> getCustomerIdsByTemplateId(Long templateId);
}
