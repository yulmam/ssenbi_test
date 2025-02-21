package com.haneolenae.bobi.domain.custom.service.port;

import java.util.List;
import java.util.Set;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.TemplateCustomerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TemplateCustomerRepository extends CrudRepository<TemplateCustomerEntity, Long> {

	List<TemplateCustomerEntity> findByCustomerIdIn(List<Long> customerIds);

	void deleteByCustomTemplateId(Long customTemplateId);

	void deleteByCustomerId(Long customerId);

	void deleteByCustomerIdAndCustomTemplateId(Long customerId, Long customTemplateId);

	Set<Long> getCustomerIdsByTemplateId(Long templateId);
}
