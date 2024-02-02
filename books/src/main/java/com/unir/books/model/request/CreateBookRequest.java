package com.unir.books.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

	private String name;
	private String author;
	private String description;
	private Long isbn;
	private String genre;
	private String language;
	private String image;
	private Boolean visible;
}
