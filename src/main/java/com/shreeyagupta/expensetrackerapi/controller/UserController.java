package com.shreeyagupta.expensetrackerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shreeyagupta.expensetrackerapi.entity.User;
import com.shreeyagupta.expensetrackerapi.entity.UserModel;
import com.shreeyagupta.expensetrackerapi.exceptions.ResourceNotFoundException;
import com.shreeyagupta.expensetrackerapi.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUser(){ //PathVariable takes value from API request and puts in java variable
		return new ResponseEntity<User>(userService.readUser(),HttpStatus.OK);
	}
	
	@PutMapping("/profile") //the RequestBody User user is what we give in Postman in body as JSON
	public ResponseEntity<User> updateUser(@RequestBody UserModel user){
		return new ResponseEntity<User>(userService.updateUser(user),HttpStatus.OK);
	}
	
	@DeleteMapping("/deactivate")
	public ResponseEntity<HttpStatus>deleteUser(){
		userService.deleteUser();
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
}
