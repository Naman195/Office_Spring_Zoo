package com.example.naman.utils;

//import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OtpHelper {

	private static final long OTP_LEN = 5;
	private static final long EXPIRATION_TIME_In_MilliSec = 5*60*1000;
	
	private final Map<String, OtpEntry> otpStore = new HashMap<>();
	
	private static class OtpEntry {
		String otp;
		long expirationTime;
		
		public OtpEntry(String otp) {
			this.otp = otp;
			this.expirationTime = System.currentTimeMillis() + EXPIRATION_TIME_In_MilliSec;
		}
		
		boolean isExpired() {
			return System.currentTimeMillis() > expirationTime;
		}
		
	}
	
	public String generateOtp() {
		Random random = new Random();
		StringBuilder otp = new StringBuilder();
		
		for(int i=0; i < OTP_LEN; i++) {
			int digit  = random.nextInt(10);
			otp.append(digit);
		}
		return otp.toString();
	}
	
	public void storeOtp(String email, String otp) {
		otpStore.put(email, new OtpEntry(otp));
	}
	
	
	public boolean validateOtp(String email, String otp) {
		OtpEntry entry = otpStore.get(email);
		
		if(entry!= null && !entry.isExpired() && entry.otp.equals(otp)) {
			otpStore.remove(email);
			return true;
		}
		return false;
	}
	
	public void removedExpiredOtps() {
		otpStore.entrySet().removeIf(entry -> entry.getValue().isExpired());
	}
	
	
}
