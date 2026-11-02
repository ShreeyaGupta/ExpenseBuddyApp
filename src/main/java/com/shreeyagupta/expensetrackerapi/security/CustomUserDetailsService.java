package com.shreeyagupta.expensetrackerapi.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shreeyagupta.expensetrackerapi.entity.User;
import com.shreeyagupta.expensetrackerapi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService { 
//Find a user in YOUR database using email and give that user's login information to Spring Security.
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User existingUser=userRepository
				          .findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found for the email "+email));
	    return new org.springframework.security.core.userdetails.User(existingUser.getEmail(),existingUser.getPassword(),new ArrayList<>());
	}

}
