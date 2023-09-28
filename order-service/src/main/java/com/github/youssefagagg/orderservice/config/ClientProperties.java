package com.github.youssefagagg.orderservice.config;

import java.net.URI;



import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "bookshop")
public record ClientProperties (

	@NotNull
	URI catalogServiceUri

){}