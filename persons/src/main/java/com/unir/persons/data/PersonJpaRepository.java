package com.unir.persons.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unir.persons.model.pojo.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface PersonJpaRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

	List<Person> findByName(String name);

	List<Person> findByAuthor(String author);

	List<Person> findByVisible(Boolean visible);

	List<Person> findByNameAndAuthor(String name, String author);

}
