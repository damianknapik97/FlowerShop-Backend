package com.dknapik.flowershop.viewmodel.account;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PasswordViewModel {
	
	private UUID id;
	
	@NotEmpty
	@Size(min = 8)
	@Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
	private String password;
	
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
