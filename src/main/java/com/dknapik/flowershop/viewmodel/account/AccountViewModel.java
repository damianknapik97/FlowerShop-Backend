package com.dknapik.flowershop.viewmodel.account;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.dknapik.security.UserRoles;

public class AccountViewModel {
	
	private UUID id;
	
	@NotEmpty
	@Size(min = 4)
	private String name;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	@Size(min = 8)
	@Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
	private String password;
	
	private String role = UserRoles.USER;

	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
