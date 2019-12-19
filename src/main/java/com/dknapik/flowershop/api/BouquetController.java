package com.dknapik.flowershop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dknapik.flowershop.services.BouquetService;

@RestController
@RequestMapping("/products/bouquets")
@CrossOrigin
public class BouquetController {
	private final BouquetService bouquetService;

	@Autowired
	public BouquetController(BouquetService bouquetService) {
		this.bouquetService = bouquetService;
	}

}
