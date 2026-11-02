package com.shreeyagupta.expensetrackerapi.service;

import com.shreeyagupta.expensetrackerapi.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shreeyagupta.expensetrackerapi.entity.User;
import com.shreeyagupta.expensetrackerapi.entity.UserModel;
import com.shreeyagupta.expensetrackerapi.exceptions.ItemAlreadyExistsException;
import com.shreeyagupta.expensetrackerapi.exceptions.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private UserRepository userRepository; 
	
	@Override
	public User createUser(UserModel user) {
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new ItemAlreadyExistsException("User is already registered with email "+user.getEmail());
		}
		User newUser=new User();
		BeanUtils.copyProperties(user, newUser);
		newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
		return userRepository.save(newUser);
	}

	@Override
	public User readUser(){
		Long userId=getLoggedInUser().getId();
		return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found for id "+userId));
	}

	@Override
	public User updateUser(UserModel user) throws ResourceNotFoundException {
		User oUser=readUser();
		oUser.setName(user.getName()!=null?user.getName():oUser.getName());
		oUser.setEmail(user.getEmail()!=null?user.getEmail():oUser.getEmail());
		oUser.setPassword(user.getPassword()!=null?bcryptEncoder.encode(user.getPassword()):oUser.getPassword());
		oUser.setAge(user.getAge()!=null?user.getAge():oUser.getAge());
		return userRepository.save(oUser);
	}

	@Override
	public void deleteUser() {
		User user=readUser();
		userRepository.delete(user);
	}

	@Override
	public User getLoggedInUser() { //Who is currently logged in? Give me that user's record from my database.
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication(); //SecurityContextHolder contains information about the currently authenticated user.
		String email=authentication.getName();
		return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found for the email "+email));
	}

}
