package com.dknapik.flowershop.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dknapik.flowershop.database.BouquetRepository;
import com.dknapik.flowershop.dto.BouquetDto;
import com.dknapik.flowershop.model.Bouquet;
import com.dknapik.flowershop.model.FlowerPack;

@Service
public class BouquetService {
	
	private BouquetRepository bouquetRepo;
	
	@Autowired
	public BouquetService(BouquetRepository bouquetRepo) {
		this.bouquetRepo = bouquetRepo;
	}
	
	public Money countTotalBouquetCost(Bouquet bouquet) {
		Money totalCost = bouquet.getWorkCost();
		
		Set<FlowerPack> flowerList = bouquet.getFlowersSet();
		
		for (FlowerPack pack : flowerList) {
			
			int numberOfFlowers = pack.getNumberOfFlowers();
			Money flowerPrice = pack.getFlower().getPrice();
			flowerPrice = flowerPrice.multiply(numberOfFlowers);
			
			totalCost.add(flowerPrice);
		}
		
		Money discount = totalCost.divide(bouquet.getTotalPriceDiscount());
		
	 	totalCost = totalCost.subtract(discount);
		return totalCost.abs();
	}
	
	public List<BouquetDto> getAllBouquets() {
		List<BouquetDto> mappedBouquetList = new LinkedList<>();
		List<Bouquet> bouquetList = this.bouquetRepo.findAll();
		
		for (Bouquet bouquet : bouquetList) {
			mappedBouquetList.add(new BouquetDto(bouquet.getId(), bouquet.getName(), countTotalBouquetCost(bouquet).toString()));
		}
		
		return mappedBouquetList;
	}
	

}
