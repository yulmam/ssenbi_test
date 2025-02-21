package com.haneolenae.bobi.domain.custom.infrastructure;


import com.haneolenae.bobi.domain.custom.infrastructure.entity.CustomTemplateEntity;
import com.haneolenae.bobi.domain.custom.service.port.CustomTemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class CustomTemplateRepositoryImpl implements CustomTemplateRepository {

    CustomTemplateJPARepository customTemplateJPARepository;

    @Override
    public Optional<CustomTemplate> findTemplateWithCustomersAndTags(List<Long> templateIds) {
        return customTemplateJPARepository.findTemplateWithCustomersAndTags(templateIds).map();
    }

    @Override
    public Page<CustomTemplateEntity> findTemplates(Pageable pageable, List<Long> templateTags, List<Long> templateCustomer, String templateSearch, long memberId) {
        return null;
    }

    @Override
    public Optional<CustomTemplateEntity> findByIdAndMemberId(long id, long memberId) {
        return Optional.empty();
    }

    @Override
    public Set<Long> findTagIdsByTemplateIdAndMemberId(Long templateId, Long memberId) {
        return Set.of();
    }
}
