package com.unir.books.data;

import com.unir.books.data.utils.SearchCriteria;
import com.unir.books.data.utils.SearchOperation;
import com.unir.books.data.utils.SearchStatement;
import com.unir.books.model.pojo.Book;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final BookJpaRepository repository;

    public List<Book> getBooks() {
        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public List<Book> search(String name, String author, String description, Long isbn, String genre, String language,
                             String image, Boolean visible) {

        SearchCriteria<Book> spec = new SearchCriteria<>();
        if (StringUtils.isNotBlank(name)) {
            spec.add(new SearchStatement("name", name, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(author)) {
            spec.add(new SearchStatement("author", author, SearchOperation.MATCH));
        }

        if (isbn != null) {
            spec.add(new SearchStatement("isbn", isbn, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(genre)) {
            spec.add(new SearchStatement("genre", genre, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(language)) {
            spec.add(new SearchStatement("language", language, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(description)) {
            spec.add(new SearchStatement("description", description, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(image)) {
            spec.add(new SearchStatement("image", image, SearchOperation.EQUAL));
        }

        if (visible != null) {
            spec.add(new SearchStatement("visible", visible, SearchOperation.EQUAL));
        }
        return repository.findAll(spec);
    }

}
