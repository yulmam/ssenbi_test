package com.haneolenae.bobi.domain.custom.infrastructure.entity;

import com.haneolenae.bobi.domain.customer.entity.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class TemplateCustomerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "custom_template_id")
	private CustomTemplateEntity customTemplateEntity;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	public TemplateCustomerEntity(CustomTemplateEntity customTemplateEntity, Customer customer) {
		this.customTemplateEntity = customTemplateEntity;
		this.customer = customer;
	}
}
