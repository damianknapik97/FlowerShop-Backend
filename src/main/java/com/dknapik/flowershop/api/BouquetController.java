package com.dknapik.flowershop.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dknapik.flowershop.dto.BouquetDto;
import com.dknapik.flowershop.services.BouquetService;


@CrossOrigin
@RestController
@RequestMapping("/products/flower")
public class BouquetController {
	BouquetService bouquetService;

	@Autowired
	public BouquetController(BouquetService bouquetService) {
		this.bouquetService = bouquetService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<BouquetDto>> getAllBouquets() {
		return new ResponseEntity<>(this.bouquetService.getAllBouquets(), HttpStatus.OK);
	}
	
	
}
