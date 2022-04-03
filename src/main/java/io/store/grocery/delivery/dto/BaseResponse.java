package io.store.grocery.delivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class BaseResponse {
	@JsonProperty("responseCode")
	private String responseCode;
	@JsonProperty("responseMsg")
	private String responseMsg;
}
