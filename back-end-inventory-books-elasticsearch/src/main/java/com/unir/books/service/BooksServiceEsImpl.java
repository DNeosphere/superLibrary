package com.unir.books.service;

import com.unir.books.model.response.ProductsQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.unir.books.data.DataAccessRepository;
import com.unir.books.model.db.Book;
import com.unir.books.model.request.CreateBookRequest;

@Service
@RequiredArgsConstructor
public class BooksServiceEsImpl implements BooksServiceEs {

	private final DataAccessRepository repository;

	@Override
	public ProductsQueryResponse getBooks(String name, String description, Long isbn, String genre, String language, String author, Boolean aggregate) {
		//Ahora por defecto solo devolvera productos visibles
		return repository.findBooks(name, description, isbn, genre, language, author, aggregate);
	}

	@Override
	public Book getBook(String productId) {
		return repository.findById(productId).orElse(null);
	}

	@Override
	public Boolean removeBook(String productId) {

		Book book = repository.findById(productId).orElse(null);
		if (book != null) {
			repository.delete(book);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Book createBook(CreateBookRequest request) {

		if (request != null && StringUtils.hasLength(request.getName().trim())
				&& StringUtils.hasLength(request.getDescription().trim())
				&& request.getIsbn() != null
				&& StringUtils.hasLength(request.getGenre().trim())
				&& StringUtils.hasLength(request.getLanguage().trim())
				&& StringUtils.hasLength(request.getAuthor().trim()) && request.getVisible() != null) {

			Book book = Book.builder()
					.name(request.getName())
					.description(request.getDescription())
					.genre(request.getGenre())
					.author(request.getAuthor())
					.isbn(request.getIsbn())
					.language(request.getLanguage())
					.visible(request.getVisible()).build();

			return repository.save(book);
		} else {
			return null;
		}
	}

}




