package com.dknapik.flowershop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.dknapik.flowershop.database.BouquetRepository;

@RestController
@CrossOrigin
public class BouquetController {
	
	BouquetRepository bouquetRepo;

	@Autowired
	public BouquetController(BouquetRepository bouquetRepo) {
		this.bouquetRepo = bouquetRepo;
	}
	
}
