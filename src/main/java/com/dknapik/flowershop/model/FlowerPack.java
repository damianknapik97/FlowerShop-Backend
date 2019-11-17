package com.dknapik.flowershop.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Represents flower with qunatity.
 * Used for bouquet construction.
 * 
 * @author Damian
 *
 */
@Entity
public class FlowerPack {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@OneToOne()
	@JoinColumn(name = "flower_id", referencedColumnName = "id")
	private Flower flower;
	@Column
	private int numberOfFlowers;
	
	public FlowerPack(Flower flower, int numberOfFlowers) {
		this.flower = flower;
		this.numberOfFlowers = numberOfFlowers;
	}
	
	public FlowerPack() { }
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Flower getFlower() {
		return flower;
	}

	public void setFlower(Flower flower) {
		this.flower = flower;
	}
	public int getNumberOfFlowers() {
		return numberOfFlowers;
	}
	public void setNumberOfFlowers(int numberOfFlowers) {
		this.numberOfFlowers = numberOfFlowers;
	}

	@Override
	public String toString() {		
		return this.flower.getName() + " - " + this.numberOfFlowers;
	}

	/**
	 * Ensuring that this class won't accidently double in set collection.
	 */
	@Override
	public boolean equals(Object arg0) {
		
		if(arg0 == null) return false;
		if(arg0 == this) return true;
		if(!(arg0 instanceof FlowerPack)) return false;
		
		FlowerPack pack = (FlowerPack) arg0;
		
		return pack.flower.getName().contentEquals(flower.getName()) && pack.numberOfFlowers == numberOfFlowers;
	}

	/**
	 * Ensuring that this class won't accidently double in set collection using hash code.
	 */
	@Override
	public int hashCode() {
			int result = 17;
			result = 31 * result + flower.getName().hashCode();
			result = 31 * result + numberOfFlowers;
		return result;
	}
}
