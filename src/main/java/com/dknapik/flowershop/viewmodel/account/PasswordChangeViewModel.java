package com.dknapik.flowershop.viewmodel.account;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PasswordChangeViewModel {
	
	private UUID id;
	
	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
	private String currentPassword;
	
	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
	private String newPassword;
	
	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
	private String newPasswordConfirmation;

	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}

	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}

	
}
