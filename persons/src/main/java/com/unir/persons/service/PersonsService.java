package com.unir.persons.service;

import java.util.List;

import com.unir.persons.model.pojo.Person;
import com.unir.persons.model.pojo.PersonDto;
import com.unir.persons.model.request.CreatePersonRequest;

public interface PersonsService {
	
	List<Person> getPersons(String name, String lastname, Boolean visible);

	Person getPerson(String personId);
	
	Boolean removePerson(String personId);

	Person createPerson(CreatePersonRequest request);

	Person updatePerson(String personId, String updateRequest);

	Person updatePerson(String personId, PersonDto updateRequest);

}
