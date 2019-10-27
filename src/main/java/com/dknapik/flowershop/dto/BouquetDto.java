package com.dknapik.flowershop.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

/**
 * Used for displaying most essential bouquet informations
 * 
 * @author Damian
 *
 */
public class BouquetDto {
	private UUID id;
	@NotBlank
	private String name;
	@NotBlank
	private String totalCost;
	
	public BouquetDto(UUID id, String name, String totalCost) {
		this.id = id;
		this.name = name;
		this.totalCost = totalCost;
	}
	
	public BouquetDto() {}
	
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
	
	public String getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	
}
