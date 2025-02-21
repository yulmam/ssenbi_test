package com.haneolenae.bobi.domain.custom.service.port;

import java.util.List;
import java.util.Set;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.TemplateTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TemplateTagRepository extends JpaRepository<TemplateTagEntity, Long> {
	List<TemplateTagEntity> findByTagIdIn(List<Long> tagIds);

	//memberId 추가 해야한다.
	@Modifying
	@Query("DELETE FROM TemplateTagEntity t WHERE t.customTemplate.id = :customTemplateId")
	void deleteByCustomTemplateId(Long customTemplateId);

	@Query
	void deleteByTagId(Long tagId);

	void deleteByTagIdAndCustomTemplateId(Long tagId, Long customTemplateId);

	@Query("SELECT t.tag.id FROM TemplateTagEntity t WHERE t.customTemplate.id = :templateId")
	Set<Long> getTagIdsByTemplateId(long templateId);
}
