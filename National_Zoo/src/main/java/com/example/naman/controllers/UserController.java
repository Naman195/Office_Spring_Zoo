package com.example.naman.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.CreateUserDTO;
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

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/user/create")
	public ResponseEntity<String> createUser(@RequestBody CreateUserDTO user) {
		try {
			userService.createUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created");
		} catch (ResponseStatusException e) {
			String errorMessage = e.getReason();

			UserResponse errorResponse = new UserResponse();

			errorResponse.setMessage(errorMessage); 

			return ResponseEntity.status(e.getStatusCode()).body(errorMessage);
		}
	}



	@PostMapping("/user/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody CredentialsDTO credentials) {
		try {
			UserResponse authenticatedUser = userService.LoginUser(credentials.getUsername(), credentials.getPassword());
			User user = userService.findByUsername(credentials.getUsername());
			String jwtToken = jwtService.generateToken(user);
			authenticatedUser.setToken(jwtToken);
			return ResponseEntity.ok(authenticatedUser);
		} catch (ResponseStatusException e) {

			String errorMessage = e.getReason();

			UserResponse errorResponse = new UserResponse();

			errorResponse.setMessage(errorMessage); 
			//            System.out.println(errorResponse.toString());


			return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
		}
	}

	@GetMapping("/allusers")
	public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
		List<ResponseUserDTO> users = userService.getAllUser();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/user/{id}")
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


	@PreAuthorize("hasRole('admin')")
	@PutMapping("/user/update/{id}")
	public ResponseEntity<?> updateUserById(@Valid @RequestBody User user, @PathVariable Long id) {
		try {
			User updatedUser = userService.UpdateUserById(user, id);
			return ResponseEntity.ok(updatedUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User update failed: " + e.getMessage());
		}
	}

	// Partial Update User
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/userupdate/{id}")
	public ResponseEntity<?> partialUpdateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
		try {
			ResponseUserDTO updatedUser = userService.partialUpdateUserById(id, updateUserDTO);
			return ResponseEntity.ok(updatedUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Partial update failed: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/user/delete/{id}")
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

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("message", "Forgot password request failed: " + e.getMessage()));
		}
	}

	@PostMapping("/verifyotp")
	public ResponseEntity<Map<String, String>> validateOtp(@RequestBody OtpResponseDTO request) {
		try {
			return userService.verifyOtp(request.getEmail(), request.getOtp());
			//			return userService.verifyOtp(request.getEmail(), request.getOtp());
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
