package com.haneolenae.bobi.domain.message.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.haneolenae.bobi.domain.message.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	@Query("SELECT DISTINCT m FROM Message m " +
		"LEFT JOIN m.messageCustomers mc " +
		"LEFT JOIN m.messageTags mt " +
		"WHERE (:keyword IS NULL OR mc.name LIKE %:keyword% " +
		"OR m.content LIKE %:keyword% " +
		"OR mt.name LIKE %:keyword%) " +
		"AND m.member.id = :memberId")
	Page<Message> findMessagesByKeywordAndMemberId(@Param("keyword") String keyword,
		@Param("memberId") Long memberId, Pageable pageable);

	Optional<Message> findByIdAndMemberId(Long messageId, Long memberId);

	List<Message> findByMemberId(Long memberId);

	@Query("SELECT DISTINCT m FROM Message m " +
		"LEFT JOIN FETCH m.messageTags mt " +
		"WHERE m.member.id = :memberId")
	List<Message> findByMemberIdWithTag(long memberId);

}
