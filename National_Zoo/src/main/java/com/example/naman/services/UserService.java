package com.example.naman.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.AddressDTO;
import com.example.naman.DTOS.CreateUserDTO;
import com.example.naman.DTOS.ResponseUserDTO;
import com.example.naman.DTOS.UpdateUserDTO;
import com.example.naman.DTOS.UserResponse;
import com.example.naman.entities.Address;
import com.example.naman.entities.City;
import com.example.naman.entities.Roles;
import com.example.naman.entities.Token;
import com.example.naman.entities.User;
import com.example.naman.enums.MessageResponse;
import com.example.naman.repositories.CityRepository;
import com.example.naman.repositories.RoleRepository;
import com.example.naman.repositories.TokenRepository;
import com.example.naman.repositories.UserRepository;
import com.example.naman.utils.OtpHelper;

import jakarta.transaction.Transactional;

/** 
 * user Service
 * 
 * @author Naman Arora
 * 
 * @since 30-dec-2024
 * 
 * */

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
	
	@Autowired
	private TokenRepository tokenRepository;
	
	private final Map<String, String> tokenStore = new HashMap<>();
	
		
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	
	/**
	 * this method is used for create new User.
	 * 
	 * @param userDTO and Image
	 * @return void
	 * @author Naman Arora
	 * 
	  */
	
	@Transactional
	public void createUser(CreateUserDTO userDTO, MultipartFile image)
		{
		
		try {
			if (userRepository.findByuserName(userDTO.getUserName()).isPresent()) {
		        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
		    }
			
			User user = modelMapper.map(userDTO, User.class); // convert user DTO  to User Class
			user.setUserId(null);
			Roles role = roleRepository.findById(userDTO.getRoleId()).get(); 
			user.setRole(role);
			String password = bcryptPasswordEncoder.encode(userDTO.getPassword());
			user.setPassword(password);
			if(image != null && !image.isEmpty()) {
	        	String imageName = saveImage(image);
	        	user.setImage(imageName);
	        }
			userRepository.save(user);
		} catch (IOException e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image: " + e.getMessage());
	    }catch (Exception e) {
	    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user: " + e.getMessage());

	    }
		
	}
	
	/** 
	 * this method is used  for save Image in the Upload Directory.
	 * @param image
	 * @return fileName
	 * 
	 * @author Naman Arora
	 * */
		private String saveImage(MultipartFile image) throws IOException
		{
		
			if (image.isEmpty()) {
				throw new IllegalArgumentException(MessageResponse.IMAGE_NULL.getMessage());
			}
			
			
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath); // if directory is not available Make it.
			}
			
			String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
			Path filePath = uploadPath.resolve(fileName); 
			Files.write(filePath, image.getBytes());
	
			return fileName.toString();	
	}
		
		
		/**
		 * this  method is used for User LoggedIn
		 * 
		 * @param username, password
		 * @return UserResponse Type DTO details.
		 * @author Naman Arora
		 *  */
	
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
	    

	    return new UserResponse(user.getUserId(), user.getUsername(), "", MessageResponse.LOGGEDIN_USER.getMessage(), userResponse, "");
	}
	
	public String logOut(String tokenHeader) {
		String tokenValue = tokenHeader.substring(7);
		Token token = tokenRepository.findByTokenValue(tokenValue);
		 tokenRepository.delete(token);
		 return "User Loggedout Successfully";
	}
	
	/** 
	 * this method is used for fetch the List of All Registered Users.
	 * @return get List of all Users.
	 * 
	 * @author Naman Arora
	 * */
	
	public List<ResponseUserDTO> getAllUser()
	{
		List<User> users =  userRepository.findAll();
		List<User> filteredusers = 	users.stream().filter(user -> !user.isArchieved()).collect(Collectors.toList());
		return filteredusers.stream()
				.map(user -> modelMapper.map(user, ResponseUserDTO.class)).collect(Collectors.toList());
	}
	
	/**
	 * this method is used for Get fetch User By ID;
	 * 
	 * @param UserId
	 * @return Get Response of User (UserResponse).
	 * 
	 * @author Naman Arora.
	 *  */
	
	public ResponseUserDTO getUserById(Long id) {
	    User existingUser = userRepository.findById(id)
	        .filter(user -> !user.isArchieved())
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found"));
	    
	    return  modelMapper.map(existingUser, ResponseUserDTO.class);

	}
	
	/**
	 * this method is used for Find User By Username.
	 * @param username
	 * @return User
	 * 
	 * @author Naman Arora 
	 * */
	
	public User findByUsername(String username) {
		return userRepository.findByuserName(username)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.USERNOTFOUND.getMessage()));
	    }
	
	/**
	 * this method is used for Update the User Details.
	 * 
	 * @param userId,  userData and user Image
	 * @return Return the Updated User.
	 * 
	 * @author Naman Arora.
	 * */
	
	 public ResponseUserDTO partialUpdateUserById(Long id, UpdateUserDTO dto, MultipartFile image) throws IOException {
	        
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.USERNOTFOUND.getMessage()));

	        
	        user.setFullName(dto.getFullName());
	        Address address = user.getAddress();
	        AddressDTO addressDTO = dto.getAddress();
	        
	        if (addressDTO != null) {
	            address.setStreet(addressDTO.getStreet());
	            address.setZipCode(addressDTO.getZipCode());

	            City city = cityRepository.findById(addressDTO.getCity().getCityId())
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.CITYNOTFOUND.getMessage()));
	            address.setCity(city);
	        }
	        
	        if(image != null && !image.isEmpty()) {
				String imageName = saveImage(image);
				user.setImage(imageName);        	
			}

	         userRepository.save(user);
	         ResponseUserDTO res = modelMapper.map(user, ResponseUserDTO.class);
	         return res;
	    }
	
		/** 
		 * this method is used for Archieved the User.
		 * @param usedId
		 * @return void
		 * 
		 *  @author Naman Arora.
		 * */
	 
	 public void deleteUserById(Long id) {
	        User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.USERNOTFOUND.getMessage()));
	        user.setArchieved(true);
	        userRepository.save(user);
	    }
	 
	 /**
		 * this method is used when user forgot their Password.
		 * @param email
		 * @return  email and message as a response.
		 * 
		 * @author Naman Arora.
		 * */

	 
	 public ResponseEntity<Map<String, String>> forgotPassword(String email) {
	        User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.INVALID_EMAIL.getMessage()));

	        String otp = otpHelper.generateOtp();
	        otpHelper.storeOtp(user.getEmail(), otp);
	       
	        senderService.sendEmail(user.getEmail(), "Your Otp Code", "Your OTP code is: " + otp);
	        
	        Map<String, String> response = new HashMap<>();
	        response.put("email", user.getEmail());
	        response.put("message", MessageResponse.OTPSENT.getMessage());
	        
	        return ResponseEntity.ok(response);
	    }
	 
	 /**
		 * 
		 * this method is used for verify Otp entered by user.
		 * @param email, otp
		 * @return get response as message and url for setPass in key-value pair.
		 * 
		 * @author Naman Arora.
		 * */
	 
	 public ResponseEntity<Map<String, String>> verifyOtp(String email, String otp){
		 boolean isValid = 	otpHelper.validateOtp(email, otp);
		 
		 if(isValid) {
			 User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.USERNOTFOUND.getMessage()));
			 
			 String forgotPasswordToken = jwtService.generateToken(user);
			 

			
	            String uniqueKey = UUID.randomUUID().toString(); 
	            tokenStore.put(uniqueKey, forgotPasswordToken);
			 
		        String resetPasswordUrl = "http://localhost:3000/setpass?token=" + forgotPasswordToken;
		        
		        Map<String, String> response = new HashMap<>();
		        response.put("message", MessageResponse.OTPVERIFY.getMessage());
//		        response.put("url", resetPasswordUrl);
		        response.put("key", uniqueKey);
		        System.out.println(uniqueKey);
		        System.out.println(tokenStore.keySet().toString());
		        return ResponseEntity.ok(response);
		    } else {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message" ,MessageResponse.OTPEXPIRE.getMessage()));
		    }
		 
	 }
	
	 
	 /**
		 * this method is used for set new password.
		 * @param tokenHeader , newPassword, oldPassword
		 * @return Message ("Password Update successfully")
		 * 
		 * @author Naman Arora
		 * */
	 
	 public String setPassword(String tokenHeader, String newPassword, String oldPassword) {
	        
			if(newPassword == null || newPassword.length() < 6) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageResponse.PASSWORD_LEN.getMessage());
			}
			
	        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token format");
	        }

	        String token = tokenHeader.substring(7);
	        String username = jwtService.extractUsername(token);
	        
	        
	        User user = userRepository.findByuserName(username)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.USERNOTFOUND.getMessage()));

	        if(oldPassword != null && !bcryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
	        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageResponse.INCORRECT_PASSWORD.getMessage());
	        }
	        
	        user.setPassword(bcryptPasswordEncoder.encode(newPassword));
	        userRepository.save(user);

	        return MessageResponse.UPDATE_PASSWORD.getMessage();
	    }
	
	 public String updatePassword(String tokenKey, String newPassword) {
		 if(newPassword == null || newPassword.length() < 6) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageResponse.PASSWORD_LEN.getMessage());
			}
		 String tokenValue = tokenStore.get(tokenKey);
			 	 
		 if (tokenValue == null ) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session Expire.");
	        }

	        String username = jwtService.extractUsername(tokenValue);
	        
	        User user = userRepository.findByuserName(username)
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageResponse.USERNOTFOUND.getMessage()));
	        
	        user.setPassword(bcryptPasswordEncoder.encode(newPassword));
	        userRepository.save(user);

	        tokenStore.clear();
	        
	        return MessageResponse.UPDATE_PASSWORD.getMessage();

	        
	 }
	
}
