package com.dknapik.flowershop.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * 
 * @author Damian
 *
 */
@Entity
public class ShoppingOrder {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	@OneToOne()
	@JoinColumn(name = "account_id", referencedColumnName = "id")
	private Account account;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "bouquet_id", referencedColumnName = "id")
	private List<Bouquet> bouquetList;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "flower_pack_id", referencedColumnName = "id")
	private Set<FlowerPack> flowerList;
	@OneToOne()
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;
	@Column
	private String noteFrom;
	@Column
	private String note;
	@Column
	private String totalPrice;
	@Column
	private boolean paid;
	
	public ShoppingOrder(UUID id,
			Account account,
			List<Bouquet> bouquetList,
			Set<FlowerPack> flowerList,
			Address address,
			String from,
			String note,
			String totalPrice,
			boolean paid) {
		this.id = id;
		this.account = account;
		this.bouquetList = bouquetList;
		this.flowerList = flowerList;
		this.address = address;
		this.noteFrom = from;
		this.note = note;
		this.totalPrice = totalPrice;
		this.paid = paid;
	}

	public ShoppingOrder() {} 
	
	public UUID getId() {
		return id;
	}

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
		return noteFrom;
	}

	public void setFrom(String from) {
		this.noteFrom = from;
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
