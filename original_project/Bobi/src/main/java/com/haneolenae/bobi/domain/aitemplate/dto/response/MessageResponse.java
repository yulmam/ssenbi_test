package com.haneolenae.bobi.domain.aitemplate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
	private String role;
	private String content;
	private String refusal;
}

