package com.example.naman.enums;

public enum MessageResponse {
	
	LOGGEDIN_USER("User Loggedin Successfully"),
	REGISTERED_USER("User registered successfully."),
	INVALID_USERNAME("Username is not Valid"),
	USERNOTFOUND("User not found"),
	CITYNOTFOUND("City not found"),
	INVALID_EMAIL("Email is not valid"),
	OTPVERIFY("OTP verified successfully"),
	OTPSENT("OTP sent to your email"),
	OTPEXPIRE("Invalid or expired OTP"),
	OTPVERIFYFAILED("Otp Verification FAiled: "),
	PASSWORD_LEN("Password must be at least 6 characters long"),
	INCORRECT_PASSWORD("Old password is incorrect"),
	UPDATE_PASSWORD("Password updated successfully"),
	ZOONOTFOUND("Zoo not found"),
	CREATE_ZOO("Zoo created successfully"),
	DELETE_ZOO("Zoo deleted successFully"),
	IMAGE_NULL("Image File is Null"),
	ANIMALNOTFOUND("Animal  Not Found"),
	TRANSFERHISTORYNOTFOUND("No Transfer Hisory Found for this Animal"),
	USERDELETE("User deleted successfully"),
	USERDELETIONFAILED("User deletion failed."),
	JSON_INVALID("Invalid JSON format"),
	ADD_ANIMAL("Animal added successfully"),
	DELETE_ANIMAL("Animal Deleted SuccessFully");
	
	
	
	
	private String message;
	
	private MessageResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	

}
