package com.dknapik.flowershop.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dknapik.flowershop.dto.FlowerDto;
import com.dknapik.flowershop.services.FlowerService;

@RestController
@RequestMapping("/products/flowers")
@CrossOrigin
public class FlowerController {
	private FlowerService flowerService;

	@Autowired
	public FlowerController(FlowerService flowerService) {
		this.flowerService = flowerService;
	}

	@GetMapping("/all")
	public ResponseEntity<List<FlowerDto>> getAllBouquet() {
		return new ResponseEntity<>(flowerService.getAllFlowers(), HttpStatus.OK);
	}
	
}
