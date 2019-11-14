package com.dknapik.flowershop.dto;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
/**
 * Used for transportation of informations related to destination of placed order.
 * 
 * @author Damian
 *
 */
public class AddressDto {
	private UUID id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String secondName;
	@NotBlank
	private String street;
	@NotBlank
	private String city;
	@NotBlank
	private String postcode;
	
	public AddressDto(UUID id,
			@NotBlank String firstName,
			@NotBlank String secondName,
			@NotBlank String street,
			@NotBlank String city,
			@NotBlank String postcode) {
		this.id = id;
		this.firstName = firstName;
		this.secondName = secondName;
		this.street = street;
		this.city = city;
		this.postcode = postcode;
	}
	
	public AddressDto(@NotBlank String firstName,
			@NotBlank String secondName,
			@NotBlank String street,
			@NotBlank String city,
			@NotBlank String postcode) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.street = street;
		this.city = city;
		this.postcode = postcode;
	}
	
	public AddressDto() {}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	
}
