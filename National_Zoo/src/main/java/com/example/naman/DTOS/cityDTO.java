package com.example.naman.DTOS;

import com.example.naman.entities.State;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class cityDTO {

	@NotNull(message = "City Id is not  Null")
	private Long cityId;
	

}
