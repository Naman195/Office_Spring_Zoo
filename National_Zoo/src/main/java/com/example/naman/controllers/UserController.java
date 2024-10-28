package com.example.naman.controllers;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.UserDTO.UpdateUserDTO;
import com.example.naman.UserDTO.UserResponse;
import com.example.naman.entities.Credentials;
import com.example.naman.entities.User;
import com.example.naman.services.JwtService;
import com.example.naman.services.UserService;

import jakarta.validation.Valid;

//@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtService jwtService;
	
	
	
	@PostMapping("/user/create")
	public ResponseEntity<?>  createUser(@RequestBody User user) {
		
		try {
	        userService.createUser(user);
	        return ResponseEntity.status(HttpStatus.CREATED).body("User SuccessFully Created");
	    } catch (Exception e) {
	        // Return a bad request or custom error message if something goes wrong
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed.");
	    }

	}
	
	@PostMapping("/user/login")
	public UserResponse LoginUser(@RequestBody Credentials cred ){		
		UserResponse authenticatedUser =  userService.LoginUser(cred.getUsername(), cred.getPassword());
		User user1 = userService.findByUsername(cred.getUsername());
		String jwtToken = jwtService.generateToken(user1);
		authenticatedUser.setToken(jwtToken);
		return authenticatedUser;
	}
	
	@GetMapping("/allusers")
	public List<User> getAllUsers()
	{
		return userService.getAllUser();
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id)
	{
		User user =  userService.getUserById(id);
		if(user.isArchieved()) {
			return ResponseEntity.status(404).body("User NOt Found");
		}
		return ResponseEntity.ok().body(user);
	}
	
	
	@PutMapping("/user/update/{id}")
	public User updateUserById(@Valid @RequestBody User user, @PathVariable Long id)
	{
		return userService.UpdateUserById(user, id);
	}
	
//	@CrossOrigin("http://localhost:3000")
	@PatchMapping("/user/delete/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
		return "User Deleted SuccessFully";
	}

	
	@PatchMapping("/userupdate/{id}")
	public ResponseEntity<User> partialUpdate(@PathVariable Long id, @RequestBody UpdateUserDTO updateUser) {
		try {
			User updatedUser = userService.partialUpdateUserById(id, updateUser);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException  e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<String> forgotPassword(@RequestBody String userName)
	{
		
		System.out.printf("UserName is", userName);
		String url = userService.forgotPassword(userName);
		return ResponseEntity.ok(url);
	}
	
	@PostMapping("/setpassword")
	public ResponseEntity<String> setPassword(@RequestHeader("Authorization") String tokenHeader
			, @RequestBody String setPassword)
	{
		String res = userService.setPassword(tokenHeader, setPassword);
		return ResponseEntity.ok(res);
	}
	
	
	
	
	

	
	
}
