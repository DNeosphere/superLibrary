package com.unir.persons.data;

import com.unir.persons.data.utils.SearchCriteria;
import com.unir.persons.data.utils.SearchOperation;
import com.unir.persons.data.utils.SearchStatement;
import com.unir.persons.model.pojo.Person;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonRepository {

    private final PersonJpaRepository repository;

    public List<Person> getPersons() {
        return repository.findAll();
    }

    public Person getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Person save(Person person) {
        return repository.save(person);
    }

    public void delete(Person person) {
        repository.delete(person);
    }

    public List<Person> search(String name, String lastname, Boolean visible) {

        SearchCriteria<Person> spec = new SearchCriteria<>();
        if (StringUtils.isNotBlank(name)) {
            spec.add(new SearchStatement("name", name, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(lastname)) {
            spec.add(new SearchStatement("author", lastname, SearchOperation.MATCH));
        }

        if (visible != null) {
            spec.add(new SearchStatement("visible", visible, SearchOperation.EQUAL));
        }
        return repository.findAll(spec);
    }

}
