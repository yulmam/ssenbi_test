package com.haneolenae.bobi.domain.custom.service.port;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.CustomTemplateEntity;

@Repository
public interface CustomTemplateRepository{

	Optional<CustomTemplateEntity> findTemplateWithCustomersAndTags(List<Long> templateIds);

	Page<CustomTemplateEntity> findTemplates(Pageable pageable, List<Long> templateTags,
											 List<Long> templateCustomer,
											 String templateSearch,
											 long memberId);

	Optional<CustomTemplateEntity> findByIdAndMemberId(long id, long memberId);

	Set<Long> findTagIdsByTemplateIdAndMemberId(
		Long templateId,
		Long memberId);

}
