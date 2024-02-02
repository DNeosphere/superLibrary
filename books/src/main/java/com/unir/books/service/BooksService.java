package com.unir.books.service;

import java.util.List;

import com.unir.books.model.pojo.Book;
import com.unir.books.model.pojo.BookDto;
import com.unir.books.model.request.CreateBookRequest;

public interface BooksService {
	
	List<Book> getBooks(String name, String author, String description, Long isbn, String genre, String language, String image, Boolean visible);

	Book getBook(String bookId);
	
	Boolean removeBook(String bookId);

	Book createBook(CreateBookRequest request);

	Book updateBook(String bookId, String updateRequest);

	Book updateBook(String bookId, BookDto updateRequest);

}
