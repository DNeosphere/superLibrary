package com.unir.persons.model.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PersonDto {
	
	private String name;
	private String lastname;
	private Boolean visible;

}
