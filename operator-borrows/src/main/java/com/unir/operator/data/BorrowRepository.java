package com.unir.operator.data;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.unir.operator.data.utils.SearchCriteria;
import com.unir.operator.data.utils.SearchOperation;
import com.unir.operator.data.utils.SearchStatement;
import com.unir.operator.model.pojo.Borrow;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BorrowRepository {

    private final BorrowJpaRepository repository;

    public Borrow save(Borrow borrow) {
        return repository.save(borrow);
    }

    public Borrow getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Borrow borrow) {
        repository.delete(borrow);
    }

	public List<Borrow> findBorrowsByPerson(Long personId){
        SearchCriteria<Borrow> spec = new SearchCriteria<>();

        if (personId != null) {
            spec.add(new SearchStatement("personId", personId, SearchOperation.EQUAL));
        }
        return repository.findAll(spec);
    }

	public List<Borrow> findBorrowsPenaltiesByPerson(Long personId){
        SearchCriteria<Borrow> spec = new SearchCriteria<>();

        if (personId != null) {
            spec.add(new SearchStatement("personId", personId, SearchOperation.EQUAL));
        }
        spec.add(new SearchStatement("returnDate", null, SearchOperation.EQUAL));
        return repository.findAll(spec);
    }
}
