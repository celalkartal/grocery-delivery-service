package io.store.grocery.delivery;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 
 * @author Celal Kartal
 *
 */

@SpringBootApplication
@EnableWebFlux
public class GroceryDeliveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroceryDeliveryServiceApplication.class, args);
	}

}
