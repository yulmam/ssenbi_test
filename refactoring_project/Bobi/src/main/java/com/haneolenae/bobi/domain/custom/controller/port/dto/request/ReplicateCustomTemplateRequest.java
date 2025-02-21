package com.haneolenae.bobi.domain.custom.controller.port.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReplicateCustomTemplateRequest {
	private boolean isReplicateTagAndCustomer;

	public boolean getIsReplicateTagAndCustomer() {
		return isReplicateTagAndCustomer;
	}
}
