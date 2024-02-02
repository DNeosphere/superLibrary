package com.unir.operator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {
	
	private Long id;
	private String name;
	private String author;
	private String description;
	private Long isbn;
	private String genre;
	private String language;
	private String image;
	private Boolean visible;
    
}
