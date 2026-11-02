package com.shreeyagupta.expensetrackerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shreeyagupta.expensetrackerapi.entity.AuthModel;
import com.shreeyagupta.expensetrackerapi.entity.JwtResponse;
import com.shreeyagupta.expensetrackerapi.entity.User;
import com.shreeyagupta.expensetrackerapi.entity.UserModel;
import com.shreeyagupta.expensetrackerapi.security.CustomUserDetailsService;
import com.shreeyagupta.expensetrackerapi.service.BlackListService;
import com.shreeyagupta.expensetrackerapi.service.UserService;
import com.shreeyagupta.expensetrackerapi.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private BlackListService blackListService;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception{
	    authenticate(authModel.getEmail(),authModel.getPassword());
	    //we need to generate jwt token 
	    final UserDetails userDetails=userDetailsService.loadUserByUsername(authModel.getEmail());
	    final String token=jwtTokenUtil.generateToken(userDetails); 
	    return new ResponseEntity<JwtResponse>(new JwtResponse(token),HttpStatus.OK);
	}
	private void authenticate(String email,String password) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
		}catch(DisabledException e) {
			throw new Exception("User disabled");
		}catch(BadCredentialsException e) {
			throw new Exception("Bad Credentials");
		}
	}
	@PostMapping("/register")
	public ResponseEntity<User> save(@Valid @RequestBody UserModel user){
		return new ResponseEntity<User>(userService.createUser(user),HttpStatus.CREATED);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PostMapping("/signout")
	public void logout(HttpServletRequest request) {
		String jwtToken=extractJwtTokenFromRequest(request);
		blackListService.addTokenToBlackList(jwtToken);
	}
	private String extractJwtTokenFromRequest(HttpServletRequest request) {
		String bearerToken=request.getHeader("Authorization");
		if(bearerToken!=null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
