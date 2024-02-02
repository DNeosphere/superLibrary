package com.unir.operator.model.pojo;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BorrowDto {
	private Long bookId;
	private Long personId;
	private LocalDateTime date;
	private Integer days;
	private LocalDateTime returnDate;
}
