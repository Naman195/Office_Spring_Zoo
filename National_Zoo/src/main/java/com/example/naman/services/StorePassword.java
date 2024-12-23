package com.example.naman.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class StorePassword {
	Map<String, String> map = new HashMap<>();
	
	public void  storeTokenUrl(String token) {
		map.put("tokenUrl", token);
	}
	
	public String getTokenByKey(String key) {
		String tokenValue = map.get(key);
		return tokenValue;
	}
		
}
