/**
 * 
 */
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
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "bouquet_id", referencedColumnName = "id")
	private Set<FlowerPack> flowersList;
	
	public Bouquet(String name, Money cost, Set<FlowerPack> flowersList) {
		super();
		this.name = name;
		this.workCost = cost.toString();
		this.flowersList = flowersList;
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
	
	public Set<FlowerPack> getFlowersList() {
		return flowersList;
	}

	public void setFlowersList(Set<FlowerPack> flowersList) {
		this.flowersList = flowersList;
	}
	
}
