package com.dknapik.flowershop.services;

import java.util.List;

import com.dknapik.flowershop.database.FlowerRepository;
import com.dknapik.flowershop.model.Flower;

public class FlowerService {
	
	private FlowerRepository flowerRepo;
	
	public FlowerService(FlowerRepository flowerRepository) {
		this.flowerRepo = flowerRepository;
	}
	
	public List<Flower> getAllFlowers() {
		return this.flowerRepo.findAll();
	}

}
