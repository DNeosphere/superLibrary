package com.unir.books.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.books.data.BookRepository;
import com.unir.books.model.pojo.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.unir.books.model.pojo.Book;
import com.unir.books.model.request.CreateBookRequest;

@Service
@Slf4j
public class BooksServiceImpl implements BooksService {

	@Autowired
	private BookRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public List<Book> getBooks(String name, String author, String description, Long isbn, String genre, String language, String image, Boolean visible) {

		if (StringUtils.hasLength(name)
				|| StringUtils.hasLength(author)
				|| StringUtils.hasLength(description)
				|| isbn != null
				|| StringUtils.hasLength(genre)
				|| StringUtils.hasLength(language)
				|| StringUtils.hasLength(image)
				|| visible != null) {
			return repository.search(name, author, description, isbn, genre, language, image, visible);
		}

		List<Book> books = repository.getBooks();
		return books.isEmpty() ? null : books;
	}

	@Override
	public Book getBook(String productId) {
		return repository.getById(Long.valueOf(productId));
	}

	@Override
	public Boolean removeBook(String productId) {

		Book product = repository.getById(Long.valueOf(productId));

		if (product != null) {
			repository.delete(product);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Book createBook(CreateBookRequest request) {


		if (request != null && StringUtils.hasLength(
				request.getName().trim())
				&& StringUtils.hasLength(request.getDescription().trim())
				&& StringUtils.hasLength(request.getGenre().trim())
				&& request.getIsbn() != null
				&& StringUtils.hasLength(request.getLanguage().trim())
				&& StringUtils.hasLength(request.getImage().trim())
				&& StringUtils.hasLength(request.getAuthor().trim())
				&& request.getVisible() != null) {

			Book book = Book.builder()
					.name(request.getName())
					.description(request.getDescription())
					.author(request.getAuthor())
					.isbn(request.getIsbn())
					.genre(request.getGenre())
					.language(request.getLanguage())
					.image(request.getImage())
					.visible(request.getVisible())
					.build();

			return repository.save(book);
		} else {
			return null;
		}
	}

	@Override
	public Book updateBook(String bookId, String request) {

		//PATCH se implementa en este caso mediante Merge Patch: https://datatracker.ietf.org/doc/html/rfc7386
		Book book = repository.getById(Long.valueOf(bookId));
		if (book != null) {
			try {
				JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
				JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
				Book patched = objectMapper.treeToValue(target, Book.class);
				repository.save(patched);
				return patched;
			} catch (JsonProcessingException | JsonPatchException e) {
				log.error("Error updating book {}", bookId, e);
                return null;
            }
        } else {
			return null;
		}
	}

	@Override
	public Book updateBook(String productId, BookDto updateRequest) {
		Book product = repository.getById(Long.valueOf(productId));
		if (product != null) {
			product.update(updateRequest);
			repository.save(product);
			return product;
		} else {
			return null;
		}
	}

}
