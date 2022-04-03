package io.store.grocery.delivery.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import io.store.grocery.delivery.document.Delivery;
import io.store.grocery.delivery.enums.TrackingStatus;
import reactor.core.publisher.Flux;

@Repository
public interface DeliveryRepository extends ReactiveMongoRepository<Delivery, String> {
	Flux<Delivery> findAllByTrackingStatus(TrackingStatus status);
}
