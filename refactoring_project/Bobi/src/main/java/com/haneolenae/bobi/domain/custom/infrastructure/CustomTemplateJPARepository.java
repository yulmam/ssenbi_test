package com.haneolenae.bobi.domain.custom.infrastructure;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.CustomTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CustomTemplateJPARepository extends JpaRepository<CustomTemplateEntity, Long> {

    @Query("SELECT t FROM CustomTemplateEntity t LEFT JOIN FETCH t.templateCustomerEntities LEFT JOIN FETCH t.templateTags WHERE t.id IN (:templateIds)")
    Optional<CustomTemplateEntity> findTemplateWithCustomersAndTags(@Param("templateIds") List<Long> templateIds);

    @Query("SELECT t FROM CustomTemplateEntity t " +
            "WHERE t.member.id = :memberId " +
            "AND (:templateTags IS NULL OR EXISTS (" +
            "    SELECT 1 FROM TemplateTagEntity tt JOIN tt.tag tag " +
            "    WHERE tt.customTemplate = t AND tag.id IN :templateTags" +
            ")) " +
            "AND (:templateCustomer IS NULL OR EXISTS (" +
            "    SELECT 1 FROM TemplateCustomerEntity tc JOIN tc.customer customer " +
            "    WHERE tc.customTemplate = t AND customer.id IN :templateCustomer" +
            ")) " +
            "AND (:templateSearch IS NULL OR " +
            "     t.title LIKE CONCAT('%', :templateSearch, '%') OR t.content LIKE CONCAT('%', :templateSearch, '%'))")
    Page<CustomTemplateEntity> findTemplates(Pageable pageable,
                                             @Param("templateTags") List<Long> templateTags,
                                             @Param("templateCustomer") List<Long> templateCustomer,
                                             @Param("templateSearch") String templateSearch,
                                             @Param("memberId") long memberId);

    Optional<CustomTemplateEntity> findByIdAndMemberId(long id, long memberId);

    @Query("SELECT DISTINCT tag.id FROM TemplateTagEntity tt " +
            "JOIN tt.tag tag " +
            "JOIN tt.customTemplate t " +
            "WHERE t.id = :templateId AND t.member.id = :memberId")
    Set<Long> findTagIdsByTemplateIdAndMemberId(
            @Param("templateId") Long templateId,
            @Param("memberId") Long memberId);

}
