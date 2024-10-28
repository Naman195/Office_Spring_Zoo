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

import com.example.naman.UserDTO.AddressDTO;
import com.example.naman.UserDTO.UpdateUserDTO;
import com.example.naman.UserDTO.UserResponse;
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
		String pass = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(pass);
		return userRepository.save(user);
	}
	
	public UserResponse LoginUser(String username, String password)
	{

	
		User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User Not Found By UserName"));
		if(user ==  null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Username");
		}
		String pass = user.getPassword();
		if(!(new BCryptPasswordEncoder().matches( password,pass)))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Password");
		}
		
		authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
		
		
		return new UserResponse(user.getUserId(), user.getUsername(), "");
	}
	
	
	
	
	public List<User> getAllUser(){
		List<User> users =  userRepository.findAll();
		List<User> filteredusers = 	users.stream().filter(user -> !user.isArchieved()).collect(Collectors.toList());
		return filteredusers;
		
	}
	
	public User getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
		return user;
	}
	
	public User findByUsername(String username) {
		User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User Not Found"));
		return user;
	}
	
	public User UpdateUserById(User user,  Long id) {
	
		User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found By Id"));
		System.out.println(existingUser);
		
		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setUserName(user.getUsername());
		existingUser.setPassword(user.getPassword());
		existingUser.setAddress(user.getAddress());
		return userRepository.save(existingUser);
		
		
	}
	
	public User partialUpdateUserById(Long id, UpdateUserDTO dto) {
		Optional<User> optionalUser = userRepository.findById(id);
		if(optionalUser.isPresent())
		{
			User user = optionalUser.get();
			Address address = user.getAddress();
		
		
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		AddressDTO addressDTO = dto.getAddress();
		address.setStreet(addressDTO.getStreet());
		address.setZipCode(addressDTO.getZipCode());
		
		City city = cityRepository.findById(addressDTO.getCity().getCityId()).orElseThrow(() -> new RuntimeException("City is not Found"));
		address.setCity(city);
		userRepository.save(user);
		return user;
		
		}
		else {
	        throw new RuntimeException("User not found");
	    }
		
		
	}
	
	public void deleteUserById(Long id) {
		User deletedUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found By Id"));
		deletedUser.setArchieved(!deletedUser.isArchieved());
		userRepository.save(deletedUser);
		
	}
	
	public String forgotPassword(String username)
	{
		User user = userRepository.findByuserName(username).orElseThrow(() -> new RuntimeException("User Name is not Valid"));
		
		String forgotPasswordToken = jwtService.generateToken(user);
		
		String url = "http://localhost:3000/setpass?token=" +  forgotPasswordToken;
		
		return  url;
	}
	
	
	
	public String setPassword(String tokenHeader, String newPassword)
	{
		
		String token = tokenHeader.substring(7);
		String username = jwtService.extractUsername(token);
		User user = userRepository.findByuserName(username).get();
		String encodedPass = bcryptPasswordEncoder.encode(newPassword);
		user.setPassword(encodedPass);
		userRepository.save(user);
		return "Password Update Successfully";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
