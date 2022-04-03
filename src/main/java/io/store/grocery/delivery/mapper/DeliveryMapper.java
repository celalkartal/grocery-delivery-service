package io.store.grocery.delivery.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.store.grocery.delivery.document.Delivery;
import io.store.grocery.delivery.dto.DeliveryUpdateResponse;
import io.store.grocery.delivery.dto.FindAllDeliveryResponse;
import io.store.grocery.delivery.dto.TrackingDto;

public class DeliveryMapper {
	public static DeliveryUpdateResponse toDeliveryUpdateResponse(Delivery delivery) {
		TrackingDto tracking = TrackingDto.builder().receivedDate(formatDate(delivery.getTracking().getReceivedDate()))
				.status(delivery.getTracking().getStatus()).build();

		return DeliveryUpdateResponse.builder().deliveryId(delivery.getDeliveryId())
				.expectedDate(delivery.getExpectedDate()).expectedWarehouse(delivery.getExpectedWarehouse())
				.product(delivery.getProduct()).quantity(delivery.getQuantity()).supplier(delivery.getSupplier())
				.tracking(tracking).build();
	}

	public static FindAllDeliveryResponse toFindAllDeliveryResponse(Delivery delivery) {
		TrackingDto tracking = TrackingDto.builder().receivedDate(formatDate(delivery.getTracking().getReceivedDate()))
				.status(delivery.getTracking().getStatus()).build();

		return FindAllDeliveryResponse.builder().deliveryId(delivery.getDeliveryId())
				.expectedDate(formatDate(delivery.getExpectedDate())).expectedWarehouse(delivery.getExpectedWarehouse())
				.product(delivery.getProduct()).quantity(delivery.getQuantity()).supplier(delivery.getSupplier())
				.tracking(tracking).build();
	}

	private static String formatDate(Date time) {
		if (time == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMdd'T'HH:mm:ss.SSSZ");
		String formattedTime = sdf.format(time);
		return formattedTime;
	}

}
