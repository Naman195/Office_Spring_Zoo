package com.example.naman.DTOS;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ApiResponse<T> {
	private boolean success;
    private String message;
    private T data;

}
