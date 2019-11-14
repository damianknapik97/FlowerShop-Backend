package com.dknapik.flowershop.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

/**
 * Used for displaying most essential informations about available flowers.
 * 
 * @author Damian
 *
 */
public class FlowerDto {
	private UUID id;
	@NotBlank
	private String name;
	@NotBlank
	private String price;
	
	public FlowerDto() {}
	
	public FlowerDto(UUID id, String name, String price) {
		this.id = id;
		this.name = name;
		this.price = price;
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

	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
}
