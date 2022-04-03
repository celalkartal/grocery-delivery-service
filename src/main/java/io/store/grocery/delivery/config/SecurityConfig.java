package io.store.grocery.delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 
 * @author Celal KARTAL
 * @apiNote permit to access the all resources for Gorillas
 * @category security
 */

@EnableWebFluxSecurity
public class SecurityConfig {
	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http.csrf().disable().authorizeExchange().anyExchange().permitAll().and().build();
	}
}
