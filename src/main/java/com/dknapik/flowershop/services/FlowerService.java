package com.dknapik.flowershop.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.dknapik.flowershop.database.FlowerPackRepository;
import com.dknapik.flowershop.model.FlowerPack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dknapik.flowershop.database.FlowerRepository;
import com.dknapik.flowershop.dto.FlowerDto;
import com.dknapik.flowershop.model.Flower;

@Service
public class FlowerService {
	private final Logger log = LogManager.getLogger(getClass().getName());
	private final FlowerRepository flowerRepo;

	@Autowired
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
