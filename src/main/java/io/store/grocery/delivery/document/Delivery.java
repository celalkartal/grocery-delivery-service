package io.store.grocery.delivery.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Delivery")
public class Delivery {
	@Id
	@JsonProperty("deliveryId")
	private String deliveryId;

	@JsonProperty("product")
	private String product;

	@JsonProperty("quantity")
	private Integer quantity;

	@JsonProperty("supplier")
	private String supplier;

	@JsonProperty("expectedDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date expectedDate;

	@JsonProperty("expectedWarehouse")
	private String expectedWarehouse;

	@NonNull
	@JsonProperty("tracking")
	private Tracking tracking;

}
