package com.unir.operator.data;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.unir.operator.model.pojo.Borrow;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface BorrowJpaRepository extends JpaRepository<Borrow, Long>, JpaSpecificationExecutor<Borrow> {

	List<Borrow> findByDate(LocalDateTime date);
}
