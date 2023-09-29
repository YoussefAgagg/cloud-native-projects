package com.github.youssefagagg.orderservice.web.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.youssefagagg.orderservice.client.BookClient;
import com.github.youssefagagg.orderservice.client.response.Book;
import com.github.youssefagagg.orderservice.domain.Order;
import com.github.youssefagagg.orderservice.enums.OrderStatus;
import com.github.youssefagagg.orderservice.event.OrderAcceptedMessage;
import com.github.youssefagagg.orderservice.web.rest.requet.OrderRequest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(TestChannelBinderConfiguration.class)
class OrderControllerTest {

    @Container
    static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutputDestination output;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookClient bookClient;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", OrderControllerTest::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgresql::getUsername);
        registry.add("spring.r2dbc.password", postgresql::getPassword);
        registry.add("spring.flyway.url", postgresql::getJdbcUrl);
    }

    private static String r2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%s/%s", postgresql.getHost(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgresql.getDatabaseName());
    }

    @Test
    void whenGetOrdersThenReturn() throws IOException {
        String bookIsbn = "1234567893";
        Book book = new Book(bookIsbn, "Title", "Author", 9.90);
        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.just(book));
        OrderRequest orderRequest = new OrderRequest(bookIsbn, 1);
        Order expectedOrder = webTestClient.post().uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();
        AssertionsForClassTypes.assertThat(expectedOrder).isNotNull();
        AssertionsForClassTypes.assertThat(objectMapper.readValue(output.receive().getPayload(), OrderAcceptedMessage.class))
                .isEqualTo(new OrderAcceptedMessage(expectedOrder.id()));

        webTestClient.get().uri("/orders")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Order.class).value(orders -> {
                    AssertionsForClassTypes.assertThat(orders.stream().filter(order -> order.bookIsbn().equals(bookIsbn)).findAny()).isNotEmpty();
                });
    }

    @Test
    void whenPostRequestAndBookExistsThenOrderAccepted() throws IOException {
        String bookIsbn = "1234567899";
        Book book = new Book(bookIsbn, "Title", "Author", 9.90);
        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.just(book));
        OrderRequest orderRequest = new OrderRequest(bookIsbn, 3);

        Order createdOrder = webTestClient.post().uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();

        AssertionsForClassTypes.assertThat(createdOrder).isNotNull();
        AssertionsForClassTypes.assertThat(createdOrder.bookIsbn()).isEqualTo(orderRequest.isbn());
        AssertionsForClassTypes.assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        AssertionsForClassTypes.assertThat(createdOrder.bookName()).isEqualTo(book.title() + " - " + book.author());
        AssertionsForClassTypes.assertThat(createdOrder.bookPrice()).isEqualTo(book.price());
        AssertionsForClassTypes.assertThat(createdOrder.status()).isEqualTo(OrderStatus.ACCEPTED);

        AssertionsForClassTypes.assertThat(objectMapper.readValue(output.receive().getPayload(), OrderAcceptedMessage.class))
                .isEqualTo(new OrderAcceptedMessage(createdOrder.id()));
    }

    @Test
    void whenPostRequestAndBookNotExistsThenOrderRejected() {
        String bookIsbn = "1234567894";
        given(bookClient.getBookByIsbn(bookIsbn)).willReturn(Mono.empty());
        OrderRequest orderRequest = new OrderRequest(bookIsbn, 3);

        Order createdOrder = webTestClient.post().uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();

        AssertionsForClassTypes.assertThat(createdOrder).isNotNull();
        AssertionsForClassTypes.assertThat(createdOrder.bookIsbn()).isEqualTo(orderRequest.isbn());
        AssertionsForClassTypes.assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        AssertionsForClassTypes.assertThat(createdOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }


}