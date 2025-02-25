package com.haneolenae.bobi.domain.message.service.port;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haneolenae.bobi.domain.message.entity.MessageTag;

public interface MessageTagRepository extends JpaRepository<MessageTag, Long> {
}
