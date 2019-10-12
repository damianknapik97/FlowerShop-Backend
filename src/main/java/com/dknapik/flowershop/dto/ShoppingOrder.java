package com.dknapik.flowershop.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.Address;
import com.dknapik.flowershop.model.Bouquet;
import com.dknapik.flowershop.model.FlowerPack;

public class ShoppingOrder {
	
	private UUID id;
	@NotNull
	private Account account;
	private List<Bouquet> bouquetList;
	private Set<FlowerPack> flowerList;
	@NotNull
	private Address address;
	@NotBlank
	private String from;
	private String note;
	private String totalPrice;
	private boolean paid = false;
	public UUID getId() {
		return id;
	}
	
	public ShoppingOrder(UUID id,
				@NotNull Account account,
						 List<Bouquet> bouquetList,
						 Set<FlowerPack> flowerList,
				@NotNull Address address,
			   @NotBlank String from,
			   			 String note,
			   			 String totalPrice,
			   			 boolean paid) {
		this.id = id;
		this.account = account;
		this.bouquetList = bouquetList;
		this.flowerList = flowerList;
		this.address = address;
		this.from = from;
		this.note = note;
		this.totalPrice = totalPrice;
		this.paid = paid;
	}
	public ShoppingOrder(@NotNull Account account,
								  List<Bouquet> bouquetList,
								  Set<FlowerPack> flowerList,
						 @NotNull Address address,
						@NotBlank String from,
						 		  String note,
						 		  String totalPrice,
						 		  boolean paid) {
		this.account = account;
		this.bouquetList = bouquetList;
		this.flowerList = flowerList;
		this.address = address;
		this.from = from;
		this.note = note;
		this.totalPrice = totalPrice;
		this.paid = paid;
	}
	
	public ShoppingOrder() {} 

	public void setId(UUID id) {
		this.id = id;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public List<Bouquet> getBouquetList() {
		return bouquetList;
	}
	public void setBouquetList(List<Bouquet> bouquetList) {
		this.bouquetList = bouquetList;
	}
	public Set<FlowerPack> getFlowerList() {
		return flowerList;
	}
	public void setFlowerList(Set<FlowerPack> flowerList) {
		this.flowerList = flowerList;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	
	


}
