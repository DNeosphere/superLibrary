package com.unir.operator.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.operator.data.BorrowRepository;
import com.unir.operator.facade.BooksFacade;
import com.unir.operator.model.pojo.Borrow;
import com.unir.operator.model.Book;
import com.unir.operator.model.pojo.BorrowDto;
import com.unir.operator.model.request.CreateBorrowRequest;

@Service
public class BorrowsServiceImpl implements BorrowsService {

    @Autowired //Inyeccion por campo (field injection). Es la menos recomendada.
    private BooksFacade booksFacade;

	@Autowired
	private BorrowRepository repository;

    @Override
    public Borrow turnIn(Long borrowId) {
        Borrow borrow = repository.getById(borrowId);
        if(borrow!=null){
            borrow.setReturnDate(LocalDateTime.now());
            repository.save(borrow);
        }
        return borrow;
    }

    @Override
    public List<Borrow> findBorrowsByPerson(Long personId) {
        return repository.findBorrowsByPerson(personId);
    }

    @Override
    public List<Borrow> findBorrowsPenaltiesByPerson(Long personId) {
        return repository.findBorrowsPenaltiesByPerson(personId);
    }

    @Override
    public Borrow getBorrow(Long borrowId) {
        return repository.getById(borrowId);
    }

    @Override
    public Boolean removeBorrow(Long borrowId) {
        Borrow borrow = repository.getById(borrowId);
        if (borrow != null) {
            Book book = booksFacade.getBook(String.valueOf(borrow.getBookId()));
            book.setVisible(true);
            repository.delete(borrow);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Borrow createBorrow(CreateBorrowRequest request) {
        Book book = booksFacade.getBook(String.valueOf(request.getBookId()));
        if(book == null || book.getVisible() != true)
            return null;
        if (request != null
                && request.getBookId() != null
                && request.getPersonId() != null
                && request.getDays() != null) {

            Borrow borrow = Borrow.builder()
                    .bookId(request.getBookId())
                    .personId(request.getPersonId())
                    .days(request.getDays())
                    .date(LocalDateTime.now())
                    .build();

            book.setVisible(false);
            return repository.save(borrow);
        } else {
            return null;
        }
    }

    @Override
    public Borrow updateBorrow(Long borrowId, BorrowDto updateRequest) {
        Borrow borrow = repository.getById(borrowId);
        if (borrow != null) {
            borrow.update(updateRequest);
            repository.save(borrow);
            return borrow;
        } else {
            return null;
        }
    }
    
}
