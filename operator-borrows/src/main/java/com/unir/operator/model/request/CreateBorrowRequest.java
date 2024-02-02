package com.unir.operator.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowRequest {
	@NotNull(message = "`book` cannot be null")
	@NotEmpty(message = "`book` cannot be empty")
	private Long bookId;
	private Long personId;
	private Integer days;
}
