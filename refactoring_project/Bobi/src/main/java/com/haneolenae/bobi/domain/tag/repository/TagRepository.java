package com.haneolenae.bobi.domain.tag.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.haneolenae.bobi.domain.member.entity.Member;
import com.haneolenae.bobi.domain.tag.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
	List<Tag> findByMemberIdAndIdIn(long memberId, List<Long> tagIds);

	@Query("SELECT t.id FROM Tag t WHERE t.member.id = :memberId AND t.id IN :tagIds")
	Set<Long> findTagIdByMemberIdAndIdIn(long memberId, List<Long> tagIds);

	List<Tag> findByMember(Member member);

	Optional<Tag> findByIdAndMemberId(Long tagId, Long memberId);

	Optional<Tag> findByNameAndMemberId(String name, Long memberId);

	@Query("SELECT t FROM Tag t " +
		"WHERE t.member.id = :memberId AND t.id IN :tagIds")
	List<Tag> findByMemberIdAndTagIds(@Param("memberId") Long memberId,
		@Param("tagIds") List<Long> tagIds);
}
