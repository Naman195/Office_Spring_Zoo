package com.example.naman.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
	private Long userId;
    private String username;
    private String token;
    private String message;
    private ResponseUserDTO user;
    private String refreshToken;
}
