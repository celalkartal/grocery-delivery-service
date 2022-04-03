package io.store.grocery.delivery.config;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.store.grocery.delivery.document.Delivery;
import io.store.grocery.delivery.document.Tracking;
import io.store.grocery.delivery.enums.AppProfiles;
import io.store.grocery.delivery.enums.TrackingStatus;
import io.store.grocery.delivery.repository.DeliveryRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Profile(AppProfiles.LOCAL)
@Slf4j
public class DeliverInitializer {
	@Bean
	public CommandLineRunner init(DeliveryRepository deliveryRepository) {
		return (args) -> {
			log.info("Started to load deliveries from 'deliveries.json' file on classpath..");
			File resource = new ClassPathResource("data/deliveries.json").getFile();
			String deliveries = new String(Files.readAllBytes(resource.toPath()));
			log.info("Loading operation from 'deliveries.json' file was completed...");

			List<Delivery> deliveryList = new ObjectMapper().readValue(deliveries, new TypeReference<List<Delivery>>() {
			});

			List<Delivery> deliveryListWithTracking = deliveryList.stream().map(delivery -> {
				Tracking dt = new Tracking();
				dt.setStatus(TrackingStatus.EXPECTED);
				delivery.setTracking(dt);
				return delivery;
			}).collect(Collectors.toList());

			log.info("Started to insert 'deliveries' to database...");
			Flux.fromStream(deliveryListWithTracking.stream()).log().map(delivery -> {
				return deliveryRepository.save(delivery);
			}).subscribe(result -> result.blockOptional());
			log.info("Insertion operation of 'deliveries' to database was completed...");
		};
	}
}
