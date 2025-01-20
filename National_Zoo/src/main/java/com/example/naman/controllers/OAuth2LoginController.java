package com.example.naman.controllers;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2LoginController {
	
	@GetMapping("/login/oauth2/code/google")
    public String googleLogin(OAuth2AuthenticationToken authentication){
		 String email = authentication.getPrincipal().getAttribute("email");
	        String fullName = authentication.getPrincipal().getAttribute("name");
	        System.out.println(email);
	        return email;
	        
	}

}
