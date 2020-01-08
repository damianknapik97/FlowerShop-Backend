package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.dto.order.ShoppingCartDetailedDto;
import com.dknapik.flowershop.services.order.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/shoppingcart")
@CrossOrigin
public class ShoppingCartController {
    private ShoppingCartService service;

    @Autowired
    public ShoppingCartController(ShoppingCartService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ShoppingCartDetailedDto> getShoppingCart(Principal principal) {
        ShoppingCartDetailedDto shoppingCartDto = service.retrieveSingleShoppingCart(principal.getName());

        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<Integer> getItemsInCart(Principal principal) {
        return null;
    }
}
