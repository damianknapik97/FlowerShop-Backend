package com.dknapik.flowershop.dto;

import javax.validation.constraints.NotBlank;

public class BouquetDto {
	
	@NotBlank
	private String id;
	private String name;
	private String totalCost;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
