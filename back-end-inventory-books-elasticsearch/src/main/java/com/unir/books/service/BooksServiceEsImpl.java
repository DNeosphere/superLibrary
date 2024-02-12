package com.unir.books.service;

import com.unir.books.model.response.ProductsQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.unir.books.data.DataAccessRepository;
import com.unir.books.model.db.Book;
import com.unir.books.model.request.CreateProductRequest;

@Service
@RequiredArgsConstructor
public class BooksServiceEsImpl implements BooksServiceEs {

	private final DataAccessRepository repository;

	@Override
	public ProductsQueryResponse getProducts(String name, String description, String isbn, String genre, String language, String author, Boolean aggregate) {
		//Ahora por defecto solo devolvera productos visibles
		return repository.findProducts(name, description, isbn, genre, language, author, aggregate);
	}

	@Override
	public Book getProduct(String productId) {
		return repository.findById(productId).orElse(null);
	}

	@Override
	public Boolean removeProduct(String productId) {

		Book book = repository.findById(productId).orElse(null);
		if (book != null) {
			repository.delete(book);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Book createProduct(CreateProductRequest request) {

		if (request != null && StringUtils.hasLength(request.getName().trim())
				&& StringUtils.hasLength(request.getDescription().trim())
				&& StringUtils.hasLength(request.getCountry().trim()) && request.getVisible() != null) {

			Book book = Book.builder().name(request.getName()).description(request.getDescription())
					.genre(request.getCountry()).visible(request.getVisible()).build();

			return repository.save(book);
		} else {
			return null;
		}
	}

}
