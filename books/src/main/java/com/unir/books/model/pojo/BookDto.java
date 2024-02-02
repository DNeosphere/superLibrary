package com.unir.books.model.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
	
	private String name;
	private String author;
	private String description;
	private Long isbn;
	private String genre;
	private String language;
	private String image;
	private Boolean visible;

}
