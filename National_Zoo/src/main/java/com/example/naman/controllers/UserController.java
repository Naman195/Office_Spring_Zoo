package com.example.naman.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.CreateUserDTO;
import com.example.naman.DTOS.CreateZooDTO;
import com.example.naman.DTOS.CredentialsDTO;
import com.example.naman.DTOS.ForgotPasswordRequestDTO;
import com.example.naman.DTOS.OtpResponseDTO;
import com.example.naman.DTOS.ResponseUserDTO;
import com.example.naman.DTOS.SetPasswordRequest;
import com.example.naman.DTOS.UpdateUserDTO;
import com.example.naman.DTOS.UserResponse;
import com.example.naman.entities.User;
import com.example.naman.services.JwtService;
import com.example.naman.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;


@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private Validator validator;

	
	@PostMapping(value = "/create", consumes = { "multipart/form-data" })
	public ResponseEntity<String> createUser(@RequestPart("user") String userJson, 
            @RequestPart(value = "file", required = false) MultipartFile file) {
		try {	
			 ObjectMapper objectMapper = new ObjectMapper();
			 
		        CreateUserDTO user = objectMapper.readValue(userJson, CreateUserDTO.class);
		        
		        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(user);
		        if(!violations.isEmpty()) {
		        	String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
		        	return
		        ResponseEntity.badRequest().body(errorMessage);
		        }
		        
			userService.createUser(user, file);
			return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created");
		}catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
	    } catch (ResponseStatusException e) {
			String errorMessage = e.getReason();

			UserResponse errorResponse = new UserResponse();

			errorResponse.setMessage(errorMessage); 

			return ResponseEntity.status(e.getStatusCode()).body(errorMessage);
		}
	}



	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody CredentialsDTO credentials) {
		try {
			UserResponse authenticatedUser = userService.loginUser(credentials.getUsername(), credentials.getPassword());
			User user = userService.findByUsername(credentials.getUsername());
			String jwtToken = jwtService.generateToken(user);
			authenticatedUser.setToken(jwtToken);
			return ResponseEntity.ok(authenticatedUser);
		} catch (ResponseStatusException e) {

			String errorMessage = e.getReason();

			UserResponse errorResponse = new UserResponse();

			errorResponse.setMessage(errorMessage); 
			return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
		}
	}

	@GetMapping("/allusers")
	public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
		List<ResponseUserDTO> users = userService.getAllUser();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		try {
			ResponseUserDTO user = userService.getUserById(id);
			return ResponseEntity.ok(user);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}


	
	@PreAuthorize("hasAuthority('update')")
	
	@PatchMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
	 
   
	public ResponseEntity<?> partialUpdateUser(@PathVariable Long id, @RequestPart("userUpdate") String userJson,  @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UpdateUserDTO user = objectMapper.readValue(userJson, UpdateUserDTO.class);
			ResponseUserDTO updatedUser = userService.partialUpdateUserById(id, user, file);
			return ResponseEntity.ok(updatedUser);
		} catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
	    } catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Partial update failed: " + e.getMessage());
		}
	}

	@PreAuthorize("hasAuthority('admin')")
	@PatchMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		try {
			userService.deleteUserById(id);
			return ResponseEntity.ok("User deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User deletion failed: " + e.getMessage());
		}
	}


	@PostMapping("/forgotpassword")
	public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
		try {
			ResponseEntity<Map<String, String>> response = userService.forgotPassword(request.getEmail());
			return response;
		} catch (ResponseStatusException e) {
			String errorMessage = e.getReason();			
			return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", errorMessage));
		}
	}

	@PostMapping("/verifyotp")
	public ResponseEntity<Map<String, String>> validateOtp(@RequestBody OtpResponseDTO request) {
		try {
			return userService.verifyOtp(request.getEmail(), request.getOtp());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Otp Verification FAiled: " + e.getMessage()));
		}
	}

	@PostMapping("/setpassword")
	public ResponseEntity<String> setPassword(@RequestHeader("Authorization") String tokenHeader,
			@RequestBody SetPasswordRequest request) {
		try {
			String response = userService.setPassword(tokenHeader, request.getNewPassword(), request.getOldPassword());
			return ResponseEntity.ok(response);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		}
	}

}
