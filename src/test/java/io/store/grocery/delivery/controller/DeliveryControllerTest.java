package io.store.grocery.delivery.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.store.grocery.delivery.document.Delivery;
import io.store.grocery.delivery.document.Tracking;
import io.store.grocery.delivery.dto.DeliveryUpdateRequest;
import io.store.grocery.delivery.dto.DeliveryUpdateResponse;
import io.store.grocery.delivery.dto.FindAllDeliveryResponse;
import io.store.grocery.delivery.enums.TrackingStatus;
import io.store.grocery.delivery.service.DeliveryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

	@Mock
	private DeliveryService deliveryService;

	@InjectMocks
	private DeliveryController deliveryController;

	private static List<Delivery> deliveryListWithTracking;

	@BeforeAll
	public static void setUp() throws Exception {
		File resource = new ClassPathResource("data/deliveries.json").getFile();
		String deliveries = new String(Files.readAllBytes(resource.toPath()));

		List<Delivery> deliveryList = new ObjectMapper().readValue(deliveries, new TypeReference<List<Delivery>>() {
		});

		deliveryListWithTracking = deliveryList.stream().map(delivery -> {
			Tracking dt = new Tracking();
			dt.setStatus(TrackingStatus.EXPECTED);
			delivery.setTracking(dt);
			return delivery;
		}).collect(Collectors.toList());
	}

	@ParameterizedTest
	@Tag("Controller")
	@MethodSource("getDataForFindDeliveriesByStatus")
	@DisplayName("DELIVERY LISTING TEST")
	@Order(2)
	void testFindDeliveriesByStatus(TrackingStatus trackingStatus) {

		var future = new CompletableFuture<List<Delivery>>();
		future.completeAsync(() -> deliveryListWithTracking, CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS));
		Flux<Delivery> fluxList = Mono.fromFuture(future).flatMapMany(Flux::fromIterable);

		when(deliveryService.findAllByTrackingStatus(trackingStatus)).thenReturn(fluxList);

		Flux<FindAllDeliveryResponse> result = deliveryController.findDeliveriesByStatus(trackingStatus);
		List<FindAllDeliveryResponse> resultList = result.collectList().block();

		assumingThat(trackingStatus.equals(TrackingStatus.EXPECTED), () -> assertTrue(resultList.size() == 8));
		assumingThat(trackingStatus.equals(TrackingStatus.RECEIVED),
				() -> assertTrue(deliveryListWithTracking.stream()
						.filter(d -> d.getTracking().getStatus().equals(TrackingStatus.RECEIVED))
						.collect(Collectors.toList()).size() == 0));

		verify(deliveryService, times(1)).findAllByTrackingStatus(trackingStatus);

	}

	public static Stream<Arguments> getDataForFindDeliveriesByStatus() {
		return Stream.of(Arguments.of(TrackingStatus.EXPECTED), Arguments.of(TrackingStatus.RECEIVED));
	}

	@ParameterizedTest
	@Tag("Controller")
	@MethodSource("getDataForUpdateDelivery")
	@DisplayName("DELIVERY UPDATE TEST")
	@Order(1)
	void testUpdateDelivery(DeliveryUpdateRequest request) {
		Delivery mainDelivery = deliveryListWithTracking.stream()
				.filter(d -> request.getDeliveryId().equals(d.getDeliveryId())).findAny().orElse(null);

		Delivery intermediateDelivery = Delivery.builder().deliveryId(mainDelivery.getDeliveryId())
				.expectedDate(mainDelivery.getExpectedDate()).expectedWarehouse(mainDelivery.getExpectedWarehouse())
				.product(mainDelivery.getProduct()).quantity(mainDelivery.getQuantity())
				.supplier(mainDelivery.getSupplier()).tracking(new Tracking()).build();
		intermediateDelivery.getTracking().setStatus(TrackingStatus.EXPECTED);

		Delivery expectedDelivery = Delivery.builder().deliveryId(mainDelivery.getDeliveryId())
				.expectedDate(mainDelivery.getExpectedDate()).expectedWarehouse(mainDelivery.getExpectedWarehouse())
				.product(mainDelivery.getProduct()).quantity(mainDelivery.getQuantity())
				.supplier(mainDelivery.getSupplier()).tracking(new Tracking()).build();

		expectedDelivery.getTracking().setReceivedDate(new Date());
		expectedDelivery.getTracking().setStatus(TrackingStatus.RECEIVED);

		when(deliveryService.findById(request.getDeliveryId())).thenReturn(Mono.just(intermediateDelivery));

		when(deliveryService.save(intermediateDelivery)).thenReturn(Mono.just(expectedDelivery));

		Mono<DeliveryUpdateResponse> result = deliveryController.updateDelivery(request);

		DeliveryUpdateResponse updatedDelivery = result.block();

		assertFalse(mainDelivery.getTracking().getStatus().equals(updatedDelivery.getTracking().getStatus()));
		assertTrue(mainDelivery.getTracking().getReceivedDate() == null);
		assertFalse(updatedDelivery.getTracking().getReceivedDate() == null);
		assertTrue(mainDelivery.getTracking().getStatus().equals(TrackingStatus.EXPECTED));
		assertTrue(updatedDelivery.getTracking().getStatus().equals(TrackingStatus.RECEIVED));

		verify(deliveryService, times(1)).findById(request.getDeliveryId());
		verify(deliveryService, times(1)).save(any(Delivery.class));
	}

	public static Stream<Arguments> getDataForUpdateDelivery() {
		return Stream.of(
				Arguments.of(DeliveryUpdateRequest.builder().deliveryId("101").trackingStatus(TrackingStatus.RECEIVED)
						.build()),
				Arguments.of(DeliveryUpdateRequest.builder().deliveryId("102").trackingStatus(TrackingStatus.RECEIVED)
						.build()));
	}

}
