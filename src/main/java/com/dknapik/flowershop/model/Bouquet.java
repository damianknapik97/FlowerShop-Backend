package com.dknapik.flowershop.model;

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

import org.javamoney.moneta.Money;

/**
 * 
 * @author Damian
 */
@Entity
public class Bouquet {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	@Column
	private String name;
	@Column
	private String workCost;
	@Column
	private int totalPriceDiscountPercent;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "bouquet_id", referencedColumnName = "id")
	private Set<FlowerPack> flowersSet; // Used to define number of specific flowers inside this bouquet
	
	public Bouquet(String name,
			Money cost,
			int totalPriceDiscountPercent,
			Set<FlowerPack> flowersSet) {
		this.name = name;
		this.workCost = cost.toString();
		this.totalPriceDiscountPercent = totalPriceDiscountPercent;
		this.flowersSet = flowersSet;
	}
	
	public Bouquet() {}

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

	public Money getWorkCost() {
		return Money.parse(workCost);
	}

	public void setWorkPrice(Money cost) {
		this.workCost = cost.toString();
	}
	
	public int getTotalPriceDiscount() {
		return totalPriceDiscountPercent;
	}

	public void setTotalPriceDiscount(int totalPriceDiscountPercent) {
		this.totalPriceDiscountPercent = totalPriceDiscountPercent;
	}

	public Set<FlowerPack> getFlowersSet() {
		return flowersSet;
	}

	public void setFlowersSet(Set<FlowerPack> flowersSet) {
		this.flowersSet = flowersSet;
	}
	
}
