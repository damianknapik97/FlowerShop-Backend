package com.dknapik.flowershop.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.javamoney.moneta.Money;

import com.dknapik.flowershop.database.BouquetRepository;
import com.dknapik.flowershop.model.Bouquet;
import com.dknapik.flowershop.model.FlowerPack;

public class BouquetService {
	
	private BouquetRepository bouquetRepo;
	
	public BouquetService(BouquetRepository bouquetRepo) {
		this.bouquetRepo = bouquetRepo;
	}
	
	public Money countTotalBouquetCost(Bouquet bouquet) {
		Money totalCost = bouquet.getWorkCost();
		
		Set<FlowerPack> flowerList = bouquet.getFlowersSet();
		
		for(FlowerPack pack : flowerList) {
			
			int numberOfFlowers = pack.getNumberOfFlowers();
			Money flowerPrice = pack.getFlower().getPrice();
			flowerPrice = flowerPrice.multiply(numberOfFlowers);
			
			totalCost.add(flowerPrice);
		}
		
		Money discount = totalCost.divide(bouquet.getTotalPriceDiscount());
		
	 	totalCost = totalCost.subtract(discount);
		return totalCost.abs();
	}
	
	public List<Bouquet> getAllBouquets() {
		return this.bouquetRepo.findAll();
	}
	

}
