package com.unir.books.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "products", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {
	
	@Id
	private String id;

	@Field(type = FieldType.Text, name = "name")
	private String name;

	@Field(type = FieldType.Text, name = "author")
	private String author;

	@Field(type = FieldType.Double, name = "isbn")
	private String isbn;
	
	@Field(type = FieldType.Keyword, name = "genre")
	private String genre;

	@Field(type = FieldType.Search_As_You_Type, name = "description")
	private String description;

	@Field(type = FieldType.Keyword, name = "language")
	private String language;
	
	@Field(type = FieldType.Boolean, name = "visible")
	private Boolean visible;

}

