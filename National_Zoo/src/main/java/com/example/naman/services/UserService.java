package com.example.naman.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.AddressDTO;
import com.example.naman.DTOS.UpdateUserDTO;
import com.example.naman.DTOS.UserResponse;
import com.example.naman.entities.Address;
import com.example.naman.entities.City;
import com.example.naman.entities.User;
import com.example.naman.repositories.CityRepository;
import com.example.naman.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	@Autowired
    private  AuthenticationManager authenticationManager;
	
	@Transactional
	public User createUser(User user) {
		if (userRepository.findByuserName(user.getUsername()).isPresent()) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
	    }
		String pass = bcryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(pass);
		
		
		return userRepository.save(user);
	}
	
	public UserResponse LoginUser(String username, String password) {
	    User user = userRepository.findByuserName(username)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

	    if (!bcryptPasswordEncoder.matches(password, user.getPassword())) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
	    }

	    authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(username, password)
	    );

	    return new UserResponse(user.getUserId(), user.getUsername(), "", "");
	}
	
	
	
	
	public List<User> getAllUser(){
		List<User> users =  userRepository.findAll();
		List<User> filteredusers = 	users.stream().filter(user -> !user.isArchieved()).collect(Collectors.toList());
		return filteredusers;
		
	}
	
//	public User getUserById(Long id) {
//		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
//		return user;
//	}
	
	public User getUserById(Long id) {
	    return userRepository.findById(id)
	        .filter(user -> !user.isArchieved())
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	}
	
	public User findByUsername(String username) {
		return userRepository.findByuserName(username)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	    }
	
	public User UpdateUserById(User user,  Long id) {
	
		User existingUser = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		
		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setUserName(user.getUsername());
		existingUser.setPassword(user.getPassword());
		existingUser.setAddress(user.getAddress());
		return userRepository.save(existingUser);
		
		
	}
	
	 public User partialUpdateUserById(Long id, UpdateUserDTO dto) {
	        // Fetch user by ID
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

	        // Update user fields
	        user.setFirstName(dto.getFirstName());
	        user.setLastName(dto.getLastName());

	        // Update address fields
	        Address address = user.getAddress();
	        AddressDTO addressDTO = dto.getAddress();
	        
	        if (addressDTO != null) {
	            address.setStreet(addressDTO.getStreet());
	            address.setZipCode(addressDTO.getZipCode());

	            City city = cityRepository.findById(addressDTO.getCity().getCityId())
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found"));
	            address.setCity(city);
	        }

	        return userRepository.save(user);
	    }
	
	 public void deleteUserById(Long id) {
	        // Fetch and toggle the archived status
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	        
	        user.setArchieved(!user.isArchieved());
	        userRepository.save(user);
	    }
	
	 public String forgotPassword(String username) {
	        // Fetch user by username
	        User user = userRepository.findByuserName(username)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username is not valid"));

	        // Generate forgot password token
	        String forgotPasswordToken = jwtService.generateToken(user);

	        // Construct the URL for resetting the password
	        return "http://localhost:3000/setpass?token=" + forgotPasswordToken;
	    }
	
	
	

	
	public String setPassword(String tokenHeader, String newPassword) {
        // Validate the token
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token format");
        }

        String token = tokenHeader.substring(7);
        String username = jwtService.extractUsername(token);
        
        // Fetch user by extracted username
        User user = userRepository.findByuserName(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Update the password
        user.setPassword(bcryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password updated successfully";
    }
	
	
}
