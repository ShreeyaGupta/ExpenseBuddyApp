package com.shreeyagupta.expensetrackerapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shreeyagupta.expensetrackerapi.security.JwtRequestFilter;

//scroll to bottom to understand flow

@Configuration //tells spring to use the below defined configuration instead of default one
public class WebSecurityConfig {
    
	@Bean
	public JwtRequestFilter authenticationJwtTokenFilter() {
		return new JwtRequestFilter();
	}
	@Bean //obj created and managed by Spring
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{ //Request from browser/postman goes to Security Filter Chain first before & then authentication and then moves to controller 
		                                          //HttpSecurity obj lets configure own security rules
		http
		   .csrf(csrf ->csrf.disable()) //csrf is security mechanism where authentication uses cookies/sessions, since we using simple browser/postman, we disable this
		   .authorizeHttpRequests(auth->auth //starting to define security rules
				   .requestMatchers("/login","/register")//if request matches login/register any user can see it (using permit all)
				   .permitAll()
				   .anyRequest().authenticated()// any other request apart from login/register like get/put/post/delete etc requires authentication
				   )
		           //JWT sessions does not require Http sessions
		           .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		           //Add JWT filter before Spring's username/password filter 
		           .addFilterBefore(authenticationJwtTokenFilter(),UsernamePasswordAuthenticationFilter.class);
		           //.httpBasic(httpBasic->{});//uses HTTP Basic authentication where client sends username+passwsord with the request
		
		
		return http.build();  // Okay, build the SecurityFilterChain using all the rules I just configured
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(
	        UserDetailsService userDetailsService,
	        PasswordEncoder passwordEncoder) {

	    DaoAuthenticationProvider provider =
	            new DaoAuthenticationProvider(userDetailsService);

	    provider.setPasswordEncoder(passwordEncoder);

	    return new ProviderManager(provider);
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() { //Spring, I'm giving you the source from which you can find authenticated users.
//		UserDetails admin=User.withUsername("shreeya")
//				              .password("{noop}12345") //noop means Do nothing to the password — don't encrypt/hash/encode it.
//				              .authorities("admin")
//				              .build();
//		UserDetails user=User.withUsername("bharath").password("{noop}12345").authorities("user")
//				              .build();
//	    return new InMemoryUserDetailsManager(admin,user); //storing these users in memory i.e. while running appn without any DB
//	}
	
}

/* 
 Postman
   ↓
email + password
   ↓
Spring Security SecurityFilterChain
   ↓
CustomUserDetailsService
   ↓
findByEmail(email)
   ↓
User table
   ↓
UserDetails
   ↓
password verification
   ↓
✅ authenticated
 */
