package com.shreeyagupta.expensetrackerapi.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class BlackListService {
 
	private Set<String>blacklist=new HashSet<>();
	public void addTokenToBlackList(String token) {
		blacklist.add(token);
	}
	public boolean isTokenBlacklisted(String token) {
		return blacklist.contains(token);
	}
}
