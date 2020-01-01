package com.dknapik.flowershop.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Represents accounts in application
 * 
 * @author Damian
 *
 */
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(unique = true)
	private String name;
	@Column
	private String password;
	@Column
	private String email;
	@Column
	private AccountRole role = AccountRole.USER;

	public Account() {}

	public Account(String password, String email, AccountRole role) {
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public Account(String name, String password, String email, AccountRole role) {
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
	}

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
	
	public AccountRole getRole() {
		return role;
	}
	
	public void setRole(AccountRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Account{" +
				"id=" + id +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", role=" + role +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Account account = (Account) o;

		if (id != null ? !id.equals(account.id) : account.id != null) return false;
		if (name != null ? !name.equals(account.name) : account.name != null) return false;
		if (password != null ? !password.equals(account.password) : account.password != null) return false;
		if (email != null ? !email.equals(account.email) : account.email != null) return false;
		return role == account.role;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (role != null ? role.hashCode() : 0);
		return result;
	}
}
