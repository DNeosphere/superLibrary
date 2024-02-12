package com.unir.books.controller;

import java.util.Map;

import com.unir.books.model.response.ProductsQueryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.books.model.db.Book;
import com.unir.books.model.request.CreateProductRequest;
import com.unir.books.service.BooksServiceEs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BooksControllerEs {

	private final BooksServiceEs service;

	@GetMapping("/books")
	public ResponseEntity<ProductsQueryResponse> getProducts(
			@RequestHeader Map<String, String> headers,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String isbn,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String genre,
			@RequestParam(required = false) String language,
			@RequestParam(required = false) String author,
			@RequestParam(required = false, defaultValue = "false") Boolean aggregate) {

		log.info("headers: {}", headers);
		ProductsQueryResponse books = service.getProducts(name, description, isbn, genre, language, author, aggregate);
		return ResponseEntity.ok(books);
	}

	@GetMapping("/books/{productId}")
	public ResponseEntity<Book> getProduct(@PathVariable String productId) {

		log.info("Request received for product {}", productId);
		Book book = service.getProduct(productId);

		if (book != null) {
			return ResponseEntity.ok(book);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@DeleteMapping("/books/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {

		Boolean removed = service.removeProduct(productId);

		if (Boolean.TRUE.equals(removed)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/books")
	public ResponseEntity<Book> getProduct(@RequestBody CreateProductRequest request) {

		Book createdBook = service.createProduct(request);

		if (createdBook != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}

}
