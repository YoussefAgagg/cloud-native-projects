package com.example.catalogservice.domian;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
@Builder
public record Book(
        @Id
        Long id,
        @NotBlank(message = "The book ISBN must be defined.")
        @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "The ISBN format must be valid.")
        String isbn,

        @NotBlank(message = "The book title must be defined.")
        String title,

        @NotBlank(message = "The book author must be defined.")
        String author,

        @NotNull(message = "The book price must be defined.")
        @Positive(message = "The book price must be greater than zero.")
        Double price,
        @Version
        int version) {
}
