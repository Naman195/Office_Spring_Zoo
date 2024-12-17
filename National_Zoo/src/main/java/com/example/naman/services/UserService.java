package com.example.naman.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.naman.utils.OtpHelper;

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
	
	@Autowired
	private OtpHelper otpHelper;
	
	@Autowired
	 private JavaSmtpGmailSenderService senderService;
	
	@Transactional
	public void createUser(CreateUserDTO userDTO) {
		if (userRepository.findByuserName(userDTO.getUserName()).isPresent()) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
	    }
		
		if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}
		
		User user = modelMapper.map(userDTO, User.class);
		Roles roles = roleRepository.findById(userDTO.getRoleId()).get();
		user.setRole(roles);
		String password = bcryptPasswordEncoder.encode(userDTO.getPassword());
		user.setPassword(password);
		userRepository.save(user);
	}
	
	public UserResponse loginUser(String username, String password) {
	    User user = userRepository.findByuserName(username)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

	    if (!bcryptPasswordEncoder.matches(password, user.getPassword())) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
	    }

	    authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(username, password)
	    );
	    
	    ResponseUserDTO userResponse = modelMapper.map(user, ResponseUserDTO.class);
	    

	    return new UserResponse(user.getUserId(), user.getUsername(), "", "User LoggedIn SuccessFully", userResponse);
	}
	
	
	
	
	public List<ResponseUserDTO> getAllUser(){
		List<User> users =  userRepository.findAll();
		
		List<User> filteredusers = 	users.stream().filter(user -> !user.isArchieved()).collect(Collectors.toList());
		
		
		List<ResponseUserDTO> allUsers = filteredusers.stream()
				.map(user -> modelMapper.map(user, ResponseUserDTO.class)).collect(Collectors.toList());
		
		return allUsers;
		
	}
	
	
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
	
	
	
	 public ResponseUserDTO partialUpdateUserById(Long id, UpdateUserDTO dto) {
	        
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

	        
	        user.setFullName(dto.getFullName());
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
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	        
	        user.setArchieved(!user.isArchieved());
	        userRepository.save(user);
	    }
	
	 public ResponseEntity<Map<String, String>> forgotPassword(String email) {
	        User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email is not valid"));

	        String otp = otpHelper.generateOtp();
	        otpHelper.storeOtp(user.getEmail(), otp);
	       
	        senderService.sendEmail(user.getEmail(), "Your Otp Code", "Your OTP code is: " + otp);
	        
	        Map<String, String> response = new HashMap<>();
	        response.put("email", user.getEmail());
	        response.put("message", "OTP sent to your email");
	        
	        return ResponseEntity.ok(response);
	    }
	 
	 public ResponseEntity<Map<String, String>> verifyOtp(String email, String otp){
		  
		 boolean isValid = 	otpHelper.validateOtp(email, otp);
		 
		 if(isValid) {
			 User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User  not found"));
			 
			 String forgotPasswordToken = jwtService.generateToken(user);
		        String resetPasswordUrl = "http://localhost:3000/setpass?token=" + forgotPasswordToken;
		        
		        Map<String, String> response = new HashMap<>();
		        response.put("message", "OTP verified successfully");
		        response.put("url", resetPasswordUrl);
		        return ResponseEntity.ok(response);
		    } else {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message" ,"Invalid or expired OTP"));
		    }
		 
	 }
	
	 
	
	public String setPassword(String tokenHeader, String newPassword, String oldPassword) {
        
		if(newPassword == null || newPassword.length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters long");
		}
		
		
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token format");
        }

        String token = tokenHeader.substring(7);
        String username = jwtService.extractUsername(token);
        
        
        User user = userRepository.findByuserName(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(oldPassword != null && !bcryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }
        
        user.setPassword(bcryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password updated successfully";
    }
	
	
}
