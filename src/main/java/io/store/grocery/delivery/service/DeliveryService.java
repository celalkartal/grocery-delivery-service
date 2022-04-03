package io.store.grocery.delivery.service;

import io.store.grocery.delivery.document.Delivery;
import io.store.grocery.delivery.enums.TrackingStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryService {
	public Flux<Delivery> findAllByTrackingStatus(TrackingStatus status);
	public Mono<Delivery> findById(String deliveryId);
	public Mono<Delivery> save(Delivery delivery);

}
