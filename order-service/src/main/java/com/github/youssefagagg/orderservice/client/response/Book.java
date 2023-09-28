package com.github.youssefagagg.orderservice.client.response;

public record Book(
	String isbn,
	String title,
	String author,
	Double price
){}