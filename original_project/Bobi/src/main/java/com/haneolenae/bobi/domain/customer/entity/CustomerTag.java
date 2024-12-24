package com.haneolenae.bobi.domain.customer.entity;

import com.haneolenae.bobi.domain.tag.entity.Tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "tag_id")
	private Tag tag;

	public CustomerTag(Customer customer, Tag tag) {
		this.customer = customer;
		this.tag = tag;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
