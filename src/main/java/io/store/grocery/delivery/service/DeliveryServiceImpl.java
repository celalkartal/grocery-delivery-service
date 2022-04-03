package io.store.grocery.delivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.store.grocery.delivery.document.Delivery;
import io.store.grocery.delivery.enums.TrackingStatus;
import io.store.grocery.delivery.repository.DeliveryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DeliveryServiceImpl implements DeliveryService {
	DeliveryRepository deliveryRepository;

	@Autowired
	public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
		this.deliveryRepository = deliveryRepository;
	}

	@Override
	public Flux<Delivery> findAllByTrackingStatus(TrackingStatus trackingStatus) {
		return deliveryRepository.findAllByTrackingStatus(trackingStatus);
	}

	@Override
	public Mono<Delivery> findById(String deliveryId) {
		return deliveryRepository.findById(deliveryId).defaultIfEmpty(new Delivery());
	}

	@Override
	public Mono<Delivery> save(Delivery delivery) {
		return deliveryRepository.save(delivery);
	}
}
