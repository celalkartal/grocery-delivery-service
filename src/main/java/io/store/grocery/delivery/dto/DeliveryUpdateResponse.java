package io.store.grocery.delivery.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
public class DeliveryUpdateResponse extends BaseResponse {
	@JsonProperty("deliveryId")
	private String deliveryId;

	@JsonProperty("product")
	private String product;

	@JsonProperty("quantity")
	private Integer quantity;

	@JsonProperty("supplier")
	private String supplier;

	@JsonProperty("expectedDate")
	private Date expectedDate;

	@JsonProperty("expectedWarehouse")
	private String expectedWarehouse;

	@JsonProperty("tracking")
	private TrackingDto tracking;
}
