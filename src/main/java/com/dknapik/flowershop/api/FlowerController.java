package com.dknapik.flowershop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.dknapik.flowershop.database.FlowerRepository;

@RestController
@CrossOrigin
public class FlowerController {
	
	FlowerRepository flowerRepo;

	@Autowired
	public FlowerController(FlowerRepository flowerRepo) {
		this.flowerRepo = flowerRepo;
	}
	
}
