package com.unir.books.model.response;

import com.unir.books.model.db.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductsQueryResponse {

    private List<Book> books;
    private List<AggregationDetails> aggs;

}
