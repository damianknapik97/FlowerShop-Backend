package com.dknapik.flowershop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dknapik.flowershop.services.FlowerService;

@RestController
@RequestMapping("/products/flowers")
@CrossOrigin
public class FlowerController {
	private final FlowerService flowerService;

	@Autowired
	public FlowerController(FlowerService flowerService) {
		this.flowerService = flowerService;
	}

}
