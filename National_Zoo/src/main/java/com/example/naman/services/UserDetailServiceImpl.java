package com.example.naman.services;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Priviledges;
import com.example.naman.entities.User;
import com.example.naman.repositories.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private RolesPriviledgesService rolesPriviledgesService;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByuserName(username).orElseThrow(()-> new RuntimeException("Username is not Valid"));
		
		Set<Priviledges> priviledges = rolesPriviledgesService.getPriviledgeForRole(user.getRole().getRoleName());
		Collection<GrantedAuthority> authorities  = priviledges.stream().map(priviledge -> new SimpleGrantedAuthority(priviledge.getPriviledges())).collect(Collectors.toList());
		user.setAuthority(authorities);
		return user;		
	}
	
	
}
