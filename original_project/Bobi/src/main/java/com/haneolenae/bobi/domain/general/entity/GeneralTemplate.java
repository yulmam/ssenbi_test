package com.haneolenae.bobi.domain.general.entity;

import java.time.LocalDateTime;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GeneralTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String content;

	@Column
	private String image;

	@Column
	private Long count;

	@Column
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Column
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	public void countUp() {
		this.count++;
	}

}
