package com.unir.operator.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowRequest {
	private Long bookId;
	private Long personId;
	private Integer days;
}
