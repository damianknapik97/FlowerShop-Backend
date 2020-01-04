package com.dknapik.flowershop.dto.account;

/**
 * Used for user authentication
 * 
 * @author Damian
 *
 */
public class LoginDto {
	private String username;
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

	@Override
	public String toString() {
		return "LoginDto{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LoginDto loginDto = (LoginDto) o;

		if (username != null ? !username.equals(loginDto.username) : loginDto.username != null) return false;
		return password != null ? password.equals(loginDto.password) : loginDto.password == null;
	}

	@Override
	public int hashCode() {
		int result = username != null ? username.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}
}
