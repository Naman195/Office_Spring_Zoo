package com.example.naman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.naman.entities.User;
import com.example.naman.enums.MessageResponse;
import com.example.naman.repositories.UserRepository;


/**
 * UserDetailsServiceImpl Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private RolesPriviledgesService rolesPriviledgesService;
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * this method is used to set Authorities .
	 * @param username,
	 * @return user
	 * 
	 * @author Naman Arora
	 * */
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByuserName(username).orElseThrow(()-> new RuntimeException(MessageResponse.INVALID_USERNAME.getMessage()));
		
		user.setAuthority(rolesPriviledgesService.getPriviledgeForRole(user.getRole()));
		
		return user;		
	}
	
	
}
