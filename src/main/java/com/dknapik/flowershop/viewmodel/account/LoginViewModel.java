package com.dknapik.flowershop.viewmodel.account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginViewModel {
	
	@NotBlank
	@Size(min = 4)
	private String username;
	
	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
