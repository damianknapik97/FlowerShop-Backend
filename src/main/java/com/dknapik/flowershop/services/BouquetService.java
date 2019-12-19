package com.dknapik.flowershop.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dknapik.flowershop.database.BouquetRepository;

@Service
public class BouquetService {
	private final Logger log = LogManager.getLogger(getClass().getName());
	private final BouquetRepository bouquetRepo;
	
	@Autowired
	public BouquetService(BouquetRepository bouquetRepo) {
		this.bouquetRepo = bouquetRepo;
	}
	/*
	public Money countTotalBouquetCost(Bouquet bouquet) {
		Money totalCost = bouquet.getWorkCost();
		Set<FlowerPack> flowerList = bouquet.getFlowersSet();
		
		for (FlowerPack pack : flowerList) {
			
			int numberOfFlowers = pack.getNumberOfFlowers();
			Money flowerPrice = pack.getFlower().getPrice();
			flowerPrice = flowerPrice.multiply(numberOfFlowers);
			
			totalCost.add(flowerPrice);
		}
		
		Money discount = totalCost.divide(bouquet.getTotalPriceDiscountPercent());
		
	 	totalCost = totalCost.subtract(discount);
		return totalCost.abs();
	}
	*/
}
