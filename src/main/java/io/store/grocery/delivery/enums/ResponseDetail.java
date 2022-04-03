package io.store.grocery.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseDetail {
	SUCCESS("00", "SUCCESS"), 
	COMMON_ERROR("01", "Error"), 
	SERVICE_TEMPORARILY_UNAVAILABLE("02","Due to received errors, the service has been temporarily shut down. Please try again later!");

	private String code;
	private String msg;

}
