package com.haneolenae.bobi.domain.message.entity;

import java.time.LocalDateTime;

import com.haneolenae.bobi.domain.message.entity.vo.CustomerInfo;
import com.haneolenae.bobi.domain.message.entity.vo.MessageSendingResult;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCustomer {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String phoneNumber;

	private String color;

	@ManyToOne
	@JoinColumn(name = "message_id")
	private Message message;

	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Column
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;

	public static MessageCustomer from(CustomerInfo customerInfo, Message message){
		return MessageCustomer.builder()
				.name(customerInfo.getName())
				.phoneNumber(customerInfo.getPhoneNumber())
				.color(customerInfo.getColor())
				.message(message)
				.build();
	}

	public static MessageCustomer from(MessageSendingResult result, Message message) {
		CustomerInfo customerInfo = result.getCustomerInfo();
		return MessageCustomer.builder()
				.name(customerInfo.getName())
				.phoneNumber(customerInfo.getPhoneNumber())
				.color(customerInfo.getColor())
				.message(message)
				.build();
	}
}
