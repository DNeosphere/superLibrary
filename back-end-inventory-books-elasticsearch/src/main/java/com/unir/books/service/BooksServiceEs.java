package com.unir.books.service;

import com.unir.books.model.db.Book;
import com.unir.books.model.request.CreateBookRequest;
import com.unir.books.model.response.ProductsQueryResponse;

public interface BooksServiceEs {

	ProductsQueryResponse getBooks(String name, String description, Long isbn, String genre, String language, String author, Boolean aggregate);

	Book getBook(String bookId);
	
	Boolean removeBook(String bookId);
	
	Book createBook(CreateBookRequest request);

}
