package io.store.grocery.delivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.store.grocery.delivery.enums.TrackingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryUpdateRequest {
	@NonNull
	@JsonProperty("deliveryId")
	private String deliveryId;

	@NonNull
	@JsonProperty("trackingStatus")
	private TrackingStatus trackingStatus;

}
