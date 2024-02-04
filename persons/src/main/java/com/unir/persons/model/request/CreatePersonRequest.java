package com.unir.persons.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePersonRequest {

	private String name;
	private String lastname;
	private Boolean visible;
}
