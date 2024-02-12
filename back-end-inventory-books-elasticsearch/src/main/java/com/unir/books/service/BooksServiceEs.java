package com.unir.books.service;

import com.unir.books.model.db.Book;
import com.unir.books.model.request.CreateProductRequest;
import com.unir.books.model.response.ProductsQueryResponse;

public interface BooksServiceEs {

	ProductsQueryResponse getProducts(String name, String description, String isbn, String genre, String language, String author, Boolean aggregate);

	Book getProduct(String bookId);
	
	Boolean removeProduct(String bookId);
	
	Book createProduct(CreateProductRequest request);

}
