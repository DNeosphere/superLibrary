package com.unir.operator.model.pojo;


import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

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
	@JsonDeserialize( using = LocalDateTimeDeserializer.class )
	@JsonSerialize( using = LocalDateTimeSerializer.class )
	private LocalDateTime date;
	private Integer days;
	@JsonDeserialize( using = LocalDateTimeDeserializer.class )
	@JsonSerialize( using = LocalDateTimeSerializer.class )
	private LocalDateTime returnDate;
}