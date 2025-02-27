package com.haneolenae.bobi.domain.message.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.haneolenae.bobi.domain.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	Member member;

	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	String content;

	@Builder.Default
	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
	private List<MessageCustomer> messageCustomers = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
	private List<MessageTag> messageTags = new ArrayList<>();

	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Column
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;



	public String makePersonalMessage(String customerName, String businessName){
		return content.replace("[[고객명]]", customerName)
				.replace("[[회사명]]", businessName);
	}
}
