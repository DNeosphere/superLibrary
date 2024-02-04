package com.unir.persons.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.persons.data.PersonRepository;
import com.unir.persons.model.pojo.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.unir.persons.model.pojo.Person;
import com.unir.persons.model.request.CreatePersonRequest;

@Service
@Slf4j
public class PersonsServiceImpl implements PersonsService {

	@Autowired
	private PersonRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public List<Person> getPersons(String name, String lastname, Boolean visible) {

		if (StringUtils.hasLength(name)
				|| StringUtils.hasLength(lastname)
				|| visible != null) {
			return repository.search(name, lastname, visible);
		}

		List<Person> persons = repository.getPersons();
		return persons.isEmpty() ? null : persons;
	}

	@Override
	public Person getPerson(String personId) {
		return repository.getById(Long.valueOf(personId));
	}

	@Override
	public Boolean removePerson(String personId) {

		Person person = repository.getById(Long.valueOf(personId));

		if (person != null) {
			repository.delete(person);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public Person createPerson(CreatePersonRequest request) {


		if (request != null && StringUtils.hasLength(
				request.getName().trim())
				&& StringUtils.hasLength(request.getLastname().trim())
				&& request.getVisible() != null) {

			Person person = Person.builder()
					.name(request.getName())
					.lastname(request.getLastname())
					.visible(request.getVisible())
					.build();

			return repository.save(person);
		} else {
			return null;
		}
	}

	@Override
	public Person updatePerson(String personId, String request) {

		//PATCH se implementa en este caso mediante Merge Patch: https://datatracker.ietf.org/doc/html/rfc7386
		Person person = repository.getById(Long.valueOf(personId));
		if (person != null) {
			try {
				JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(request));
				JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(person)));
				Person patched = objectMapper.treeToValue(target, Person.class);
				repository.save(patched);
				return patched;
			} catch (JsonProcessingException | JsonPatchException e) {
				log.error("Error updating person {}", personId, e);
                return null;
            }
        } else {
			return null;
		}
	}

	@Override
	public Person updatePerson(String personId, PersonDto updateRequest) {
		Person person = repository.getById(Long.valueOf(personId));
		if (person != null) {
			person.update(updateRequest);
			repository.save(person);
			return person;
		} else {
			return null;
		}
	}

}
