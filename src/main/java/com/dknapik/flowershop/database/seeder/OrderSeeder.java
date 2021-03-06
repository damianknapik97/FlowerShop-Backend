package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.model.order.*;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;

@ToString
class OrderSeeder implements SeederInt {
    private static final boolean ONLY_FOR_DEBUG = false;
    private final MoneyUtils moneyUtils;
    private final OrderRepository orderRepository;


    @Autowired
    public OrderSeeder(MoneyUtils moneyUtils,
                       OrderRepository orderRepository) {
        this.moneyUtils = moneyUtils;
        this.orderRepository = orderRepository;
    }

    @Override
    public void seed() {
        Money totalPrice = Money.of(5, moneyUtils.getApplicationCurrencyUnit());
        Payment payment = new Payment(totalPrice, PaymentType.BANK_TRANSFER);
        DeliveryAddress deliveryAddress =
                new DeliveryAddress("Katowice", "42-500", "Test Street", "328");
        ShoppingCart shoppingCart = new ShoppingCart("New Shopping Cart");

        /* Create new entity */
        Order newEntity = new Order("Happy New Year!", payment, deliveryAddress, shoppingCart);

        /* Save new entity */
        if (!orderRepository.exists(Example.of(newEntity))) {
            orderRepository.saveAndFlush(newEntity);
        }
    }

    @Override
    public boolean isOnlyForDebug() {
        return ONLY_FOR_DEBUG;
    }
}
