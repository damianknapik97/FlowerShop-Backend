package com.dknapik.flowershop.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dknapik.flowershop.database.FlowerRepository;
import com.dknapik.flowershop.dto.FlowerDto;
import com.dknapik.flowershop.model.Flower;

@Service
public class FlowerService {
	
	private FlowerRepository flowerRepo;
	
	
	public FlowerService(FlowerRepository flowerRepository) {
		this.flowerRepo = flowerRepository;
	}
	
	public List<FlowerDto> getAllFlowers() {
		List<FlowerDto> mappedFlowerList = new LinkedList<>();
		List<Flower> flowerList = this.flowerRepo.findAll();
		
		for (Flower flower : flowerList) {
			mappedFlowerList.add(new FlowerDto(flower.getId(),flower.getName(),flower.getPrice().toString()));
		}
		
		return mappedFlowerList;
	}

}
