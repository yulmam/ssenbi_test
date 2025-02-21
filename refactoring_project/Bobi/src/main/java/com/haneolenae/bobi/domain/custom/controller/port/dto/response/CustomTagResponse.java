package com.haneolenae.bobi.domain.custom.controller.port.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomTagResponse {
	private long tagId;
	private String tagName;
	private String tagColor;
}
