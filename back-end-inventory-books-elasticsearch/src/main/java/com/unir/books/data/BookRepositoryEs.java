package com.unir.books.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.unir.books.model.db.Book;

public interface BookRepositoryEs extends ElasticsearchRepository<Book, String> {

	List<Book> findByName(String name);
	
	Optional<Book> findById(String id);
	
	Book save(Book book);
	
	void delete(Book book);
	
	List<Book> findAll();
}
