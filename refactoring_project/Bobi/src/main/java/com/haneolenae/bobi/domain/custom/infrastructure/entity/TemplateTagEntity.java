package com.haneolenae.bobi.domain.custom.infrastructure.entity;

import com.haneolenae.bobi.domain.tag.entity.Tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TemplateTagEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "custom_template_id")
	private CustomTemplateEntity customTemplateEntity;

	@ManyToOne
	@JoinColumn(name = "tag_id")
	private Tag tag;

	public TemplateTagEntity(CustomTemplateEntity customTemplateEntity, Tag tag) {
		this.customTemplateEntity = customTemplateEntity;
		this.tag = tag;
	}
}
