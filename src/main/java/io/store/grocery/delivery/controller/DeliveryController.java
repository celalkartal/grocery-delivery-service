package io.store.grocery.delivery.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.store.grocery.delivery.dto.DeliveryUpdateRequest;
import io.store.grocery.delivery.dto.DeliveryUpdateResponse;
import io.store.grocery.delivery.dto.FindAllDeliveryResponse;
import io.store.grocery.delivery.enums.ResponseDetail;
import io.store.grocery.delivery.enums.TrackingStatus;
import io.store.grocery.delivery.mapper.DeliveryMapper;
import io.store.grocery.delivery.service.DeliveryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class DeliveryController {
	private final DeliveryService deliveryService;

	@Autowired
	public DeliveryController(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	@QueryMapping("findDeliveriesByStatus")
	Flux<FindAllDeliveryResponse> findDeliveriesByStatus(@Argument TrackingStatus trackingStatus) {
		return deliveryService.findAllByTrackingStatus(trackingStatus).map(DeliveryMapper::toFindAllDeliveryResponse);
	}

	@MutationMapping("updateDelivery")
	@CircuitBreaker(name = "updateDelivery", fallbackMethod = "updateDeliveryFallback")
	Mono<DeliveryUpdateResponse> updateDelivery(@Argument DeliveryUpdateRequest deliveryUpdateRequest) {
		return deliveryService.findById(deliveryUpdateRequest.getDeliveryId()).flatMap(n -> {
			n.getTracking().setStatus(deliveryUpdateRequest.getTrackingStatus());
			if (TrackingStatus.RECEIVED.equals(deliveryUpdateRequest.getTrackingStatus())) {
				n.getTracking().setReceivedDate(new Date());
			}
			return deliveryService.save(n);
		}).map(DeliveryMapper::toDeliveryUpdateResponse).map(n -> {
			n.setResponseCode(ResponseDetail.SUCCESS.getCode());
			n.setResponseMsg(ResponseDetail.SUCCESS.getMsg());
			return n;
		}).onErrorResume(ex -> Mono.just(DeliveryUpdateResponse.builder()
				.deliveryId(deliveryUpdateRequest.getDeliveryId())
				.responseCode(ResponseDetail.COMMON_ERROR.getCode())
				.responseMsg(ResponseDetail.COMMON_ERROR.getCode() + "" + ex.getMessage()).build()));
	}

	public Mono<DeliveryUpdateResponse> updateDeliveryFallback(Throwable ex) {
		return Mono.just(
				DeliveryUpdateResponse.builder().responseCode(ResponseDetail.SERVICE_TEMPORARILY_UNAVAILABLE.getCode())
						.responseMsg(ResponseDetail.SERVICE_TEMPORARILY_UNAVAILABLE.getMsg()).build());
	}

}
