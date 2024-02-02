package com.unir.operator.service;

import java.util.List;

import com.unir.operator.model.pojo.Borrow;
import com.unir.operator.model.pojo.BorrowDto;
import com.unir.operator.model.request.CreateBorrowRequest;

public interface BorrowsService {

	Borrow turnIn(Long borrowId);

	List<Borrow> findBorrowsByPerson(Long personId);

	List<Borrow> findBorrowsPenaltiesByPerson(Long personId);
	
	Borrow getBorrow(Long borrowId);
	
	Boolean removeBorrow(Long borrowId);

	Borrow createBorrow(CreateBorrowRequest request);

	Borrow updateBorrow(Long borrowId, BorrowDto updateRequest);

}