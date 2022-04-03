package io.store.grocery.delivery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class FindAllDeliveryResponse {
	
	@JsonProperty("deliveryId")
	private String deliveryId;

	@JsonProperty("product")
	private String product;

	@JsonProperty("quantity")
	private Integer quantity;

	@JsonProperty("supplier")
	private String supplier;

	@JsonProperty("expectedDate")
	private String expectedDate;

	@JsonProperty("expectedWarehouse")
	private String expectedWarehouse;

	@JsonProperty("tracking")
	private TrackingDto tracking;
}
