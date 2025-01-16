package com.example.naman.controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
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
import com.example.naman.DTOS.RequestTokenreq;
import com.example.naman.DTOS.ResponseUserDTO;
import com.example.naman.DTOS.UpdatePasswordDTO;
import com.example.naman.DTOS.SetPasswordDTO;
import com.example.naman.DTOS.UpdateUserDTO;
import com.example.naman.DTOS.UserResponse;
import com.example.naman.entities.RefreshToken;
import com.example.naman.entities.Token;
import com.example.naman.entities.User;
import com.example.naman.enums.MessageResponse;
import com.example.naman.repositories.TokenRepository;
import com.example.naman.services.JwtService;
import com.example.naman.services.RefreshTokenService;
import com.example.naman.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;


/** 
 * user Controller
 * 
 * @author Naman Arora
 * 
 * @since 30-dec-2024
 * 
 * */

@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	/**
	 * this controller is used for create new User.
	 * 
	 * @param userData and user Image
	 * @return Success Message ("User Created SuccessFully");
	 * @author Naman Arora
	 * 
	  */
	
	@PostMapping(value = "/create", consumes = { "multipart/form-data" })
	public ResponseEntity<String> createUser(@RequestPart("user") String userJson, 
            @RequestPart(value = "file", required = false) MultipartFile file)
	{
		try {	
			 ObjectMapper objectMapper = new ObjectMapper();
			 
		        CreateUserDTO user = objectMapper.readValue(userJson, CreateUserDTO.class);
		        
		        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(user);
		        if(!violations.isEmpty()) {
		        	String errorMessage = violations.stream().map(ConstraintViolation -> ConstraintViolation.getMessage()).collect(Collectors.joining(", "));
		        	
		        	return
		        ResponseEntity.badRequest().body(errorMessage);
		        }
		        
		        
		        
			userService.createUser(user, file);
			return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponse.REGISTERED_USER.getMessage());
		}catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body(MessageResponse.JSON_INVALID.getMessage() + e.getMessage());
	    } catch (ResponseStatusException e) {
			String errorMessage = e.getReason();

			UserResponse errorResponse = new UserResponse();

			errorResponse.setMessage(errorMessage); 

			return ResponseEntity.status(e.getStatusCode()).body(errorMessage);
		}
	}

	/**
	 * this  controller is used for User LoggedIn
	 * 
	 * @param credentials
	 * @return UserResponse Type DTO .
	 * @author Naman Arora
	 *  */

	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody CredentialsDTO credentials)
	{
		try {
			UserResponse authenticatedUser = userService.loginUser(credentials.getUsername(), credentials.getPassword());
			User user = userService.findByUsername(credentials.getUsername());
			String jwtToken = jwtService.generateToken(user);
			
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
			authenticatedUser.setRefreshToken(refreshToken.getRefreshToken());
			authenticatedUser.setToken(jwtToken);
			
			// save JWT Token in Database

			Token token = Token.builder().tokenValue(jwtToken).userId(user.getUserId()).expiresAt(jwtService.getExp(jwtToken)).build();
//			Token token = Token.builder().tokenValue(jwtToken).userId(user.getUserId()).expiresAt(LocalDateTime.now().plusHours(2)).build();
			tokenRepository.save(token);		
			return ResponseEntity.ok(authenticatedUser);
		} catch (ResponseStatusException e) {

			String errorMessage = e.getReason();

			UserResponse errorResponse = new UserResponse();

			errorResponse.setMessage(errorMessage); 
			return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
		}
	}
	
	
	
	@PostMapping("/refresh")
	public String refreshJwtToken(@RequestBody RequestTokenreq refreshToken){
		RefreshToken refreshTokenobj = refreshTokenService.verifyRefreshToken(refreshToken.getRefreshtoken());
		
		User user = refreshTokenobj.getUser();
		
		String jwtToken = jwtService.generateToken(user);
		
		Token token =  Token.builder().tokenValue(jwtToken).userId(user.getUserId()).expiresAt(jwtService.getExp(jwtToken)).build();
		tokenRepository.save(token);		
		
		return jwtToken;
		
	}
	
	@PostMapping("/logout")
	public String logout(@RequestHeader("Authorization") String tokenHeader) {
		return userService.logOut(tokenHeader);
	}
	
	/** 
	 * this controller is used for fetch the List of All Registered Users.
	 * 
	 * @return get List of all Users
	 * @author Naman Arora
	 * */

	@GetMapping("/allusers")
	public ResponseEntity<List<ResponseUserDTO>> getAllUsers()
	{
		List<ResponseUserDTO> users = userService.getAllUser();
		return ResponseEntity.ok(users);
	}
	
	/**
	 * this controller is used for Get fetch User By ID;
	 * 
	 * @param UserId
	 * @return Get Response of User (UserResponse).
	 * 
	 * @author Naman Arora.
	 *  */

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long userId)
	{
		try {
			ResponseUserDTO user = userService.getUserById(userId);
			return ResponseEntity.ok(user);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	/**
	 * this controller is used for Update the User Details.
	 * 
	 * @param userId,  userData and user Image
	 * @return Return the Updated User.
	 * 
	 * @author Naman Arora.
	 * */
	
	@PreAuthorize("hasAuthority('update')")
	@PatchMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> partialUpdateUser(@PathVariable Long id, @RequestPart("userUpdate") String userJson,  @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			UpdateUserDTO user = objectMapper.readValue(userJson, UpdateUserDTO.class);
			ResponseUserDTO updatedUser = userService.partialUpdateUserById(id, user, file);
			return ResponseEntity.ok(updatedUser);
		} catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body(MessageResponse.JSON_INVALID.getMessage() + e.getMessage());
	    } catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Partial update failed: " + e.getMessage());
		}
	}
	
	/** 
	 * this Controller is used for Archieved the User.
	 * @param usedId
	 * @return success message "User Deleted Sucessfully".
	 * 
	 *  @author Naman Arora.
	 * */

	@PreAuthorize("hasAuthority('admin')")
	@PatchMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		try {
			userService.deleteUserById(id);
			return ResponseEntity.ok(MessageResponse.USERDELETE.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.USERDELETIONFAILED.getMessage() + e.getMessage());
		}
	}
	
	/**
	 * this controller is used for forgot Password.
	 * @param request(email)
	 * @return got user Email and message as a response.
	 * 
	 * @author Naman Arora.
	 * */

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
	
	/**
	 * 
	 * this controller is used for verify Otp entered by user.
	 * @param otpResponseDTO
	 * @return get response as message and url for setPass in key-value pair.
	 * 
	 * @author Naman Arora.
	 * */

	@PostMapping("/verifyotp")
	public ResponseEntity<Map<String, String>> validateOtp(@RequestBody OtpResponseDTO request) {
		try {
			return userService.verifyOtp(request.getEmail(), request.getOtp());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", MessageResponse.OTPVERIFYFAILED.getMessage() + e.getMessage()));
		}
	}

	
	/**
	 * this controller is used for set new password.
	 * @param tokenHeader and SetPasswordRequest Body
	 * @return Message ("Password Update successfully")
	 * 
	 * @author Naman Arora
	 * */
	
	@PostMapping("/setpassword")
	public ResponseEntity<String> setPassword(@RequestBody SetPasswordDTO request) {
		try {
			String response = userService.setPassword(request.getTokenKey(), request.getNewPassword());
			return ResponseEntity.ok(response);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		}
	}
	
	@PostMapping("/updatepassword")
	public String updatePass(@RequestHeader("Authorization") String tokenHeader ,@RequestBody UpdatePasswordDTO request){
		return userService.updatePassword(tokenHeader, request.getNewPassword(), request.getOldPassword());
	}

}
