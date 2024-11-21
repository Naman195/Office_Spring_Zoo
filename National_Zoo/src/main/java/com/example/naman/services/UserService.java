package com.example.naman.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.AddressDTO;
import com.example.naman.DTOS.CreateUserDTO;
import com.example.naman.DTOS.ResponseUserDTO;
import com.example.naman.DTOS.UpdateUserDTO;
import com.example.naman.DTOS.UserResponse;
import com.example.naman.entities.Address;
import com.example.naman.entities.City;
import com.example.naman.entities.Roles;
import com.example.naman.entities.User;
import com.example.naman.repositories.CityRepository;
import com.example.naman.repositories.RoleRepository;
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
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional
	public void createUser(CreateUserDTO userDTO) {
		if (userRepository.findByuserName(userDTO.getUserName()).isPresent()) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
	    }
		
		
		User user = modelMapper.map(userDTO, User.class);
		Roles roles = roleRepository.findById(userDTO.getRoleId()).get();
//		System.out.println("Rolesi d us" + userDTO.getRole());
		user.setRole(roles);
		String pass = bcryptPasswordEncoder.encode(userDTO.getPassword());
		user.setPassword(pass);
		userRepository.save(user);
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

	    return new UserResponse(user.getUserId(), user.getUsername(), "", "User LoggedIn SuccessFully");
	}
	
	
	
	
	public List<ResponseUserDTO> getAllUser(){
		List<User> users =  userRepository.findAll();
		
		List<User> filteredusers = 	users.stream().filter(user -> !user.isArchieved()).collect(Collectors.toList());
		
		
		List<ResponseUserDTO> allUsers = filteredusers.stream()
				.map(user -> modelMapper.map(user, ResponseUserDTO.class)).collect(Collectors.toList());
		
		return allUsers;
		
	}
	

	
//	public ResponseUserDTO getUserById(Long id) {
//	    User existingUser =  userRepository.findById(id)
//	        .filter(user -> !user.isArchieved())
//	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//	    return modelMapper.map(existingUser, ResponseUserDTO.class);
//	}
	
	public ResponseUserDTO getUserById(Long id) {
	    User existingUser = userRepository.findById(id)
	        .filter(user -> !user.isArchieved())
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found"));
	    
	    ResponseUserDTO userDTo =  modelMapper.map(existingUser, ResponseUserDTO.class);
	    return userDTo;
	}
	
	public User findByUsername(String username) {
		return userRepository.findByuserName(username)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	    }
	
	public User UpdateUserById(User user,  Long id) {
	
		User existingUser = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		
		existingUser.setFullName(user.getFullName());
		existingUser.setEmail(user.getEmail());
		existingUser.setUserName(user.getUsername());
		existingUser.setPassword(user.getPassword());
		existingUser.setAddress(user.getAddress());
		return userRepository.save(existingUser);
		
		
	}
	
	 public ResponseUserDTO partialUpdateUserById(Long id, UpdateUserDTO dto) {
	        // Fetch user by ID
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

	        // Update user fields
	        user.setFullName(dto.getFullName());
	        user.setEmail(dto.getEmail());

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

	         userRepository.save(user);
	         ResponseUserDTO res = modelMapper.map(user, ResponseUserDTO.class);
	         return res;
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
