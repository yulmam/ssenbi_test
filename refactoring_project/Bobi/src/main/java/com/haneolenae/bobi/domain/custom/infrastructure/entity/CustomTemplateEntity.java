package com.haneolenae.bobi.domain.custom.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.haneolenae.bobi.domain.custom.domain.CustomTemplate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.haneolenae.bobi.domain.custom.controller.port.dto.request.EditCustomTemplateRequest;
import com.haneolenae.bobi.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NamedEntityGraph(
	name = "CustomTemplateEntity.withTagsAndCustomers",
	attributeNodes = {
		@NamedAttributeNode("templateTagEntities"),
		@NamedAttributeNode("templateCustomerEntities")
	}
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomTemplateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String content;

	@Column
	private Integer count;

	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Column
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "customTemplate", fetch = FetchType.LAZY)
	private List<TemplateCustomerEntity> templateCustomerEntities;

	@OneToMany(mappedBy = "customTemplate", fetch = FetchType.LAZY)
	private List<TemplateTagEntity> templateTagEntities;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public CustomTemplateEntity(String title, String content, Integer count) {
		this.title = title;
		this.content = content;
		this.count = count;
	}

	@Builder
	public CustomTemplateEntity(String title, String content, Integer count, Member member) {
		this.title = title;
		this.content = content;
		this.count = count;
		this.member = member;
	}

	public void editTitleAndContent(EditCustomTemplateRequest request) {
		this.title = request.getTemplateTitle();
		this.content = request.getTemplateContent();
	}

	public CustomTemplateEntity(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public CustomTemplateEntity replicateMe() {
		return CustomTemplateEntity.builder()
			.title(this.title)
			.content(this.content)
			.member(this.member)
			.count(0)
			.build();
	}

	public void countUp() {
		count++;
	}

	public CustomTemplate to(){
		return CustomTemplate.builder()
				.id(id)
				.title(title)
				.content(content)
				.count(count)
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.templateCustomer(templateCustomerEntities.to())
				.templateTagEntities(templateTagEntities.to())
				.member()
				.build();
	}
}
