package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.ShoppingCartDTO;
import com.dknapik.flowershop.mapper.order.ShoppingCartMapper;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.services.order.ShoppingCartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/shopping-cart")
@CrossOrigin
@Log4j2
public class ShoppingCartController {
    private final ShoppingCartService service;
    private final ShoppingCartMapper mapper;

    @Autowired
    public ShoppingCartController(ShoppingCartService service, ShoppingCartMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Returns detailed shopping cart instance, with all currently added products.
     *
     * @param principal - logged user
     * @return ShoppingCart Dto with products, name and id
     */
    @GetMapping
    public ResponseEntity<ShoppingCartDTO> getShoppingCart(Principal principal) {
        log.info(() -> "Processing request: " + this.getClass().getEnclosingMethod().getName() +
                " for following user " + principal.getName());

        ShoppingCart shoppingCart = service.retrieveSingleShoppingCart(principal.getName());

        log.trace("Casting retrieved results to dto");
        ShoppingCartDTO shoppingCartDto = mapper.convertToDTO(shoppingCart);

        log.trace(() -> "Building response entity for request: " + this.getClass().getEnclosingMethod().getName() +
                " for following user " + principal.getName());
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }

    /**
     * Counts number of products that are currently inside provided shopping cart id;
     *
     * @param id - UUID of shopping cart
     * @return - number of products inside shopping cart
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> countShoppingCartProducts(@Valid @RequestParam("id") UUID id) {
        log.trace(() -> "Processing request: " + this.getClass().getEnclosingMethod().getName() +
                " using following id " + id.toString());

        int numberOfProducts = service.countNumberOfProducts(id);

        log.trace(() -> "Building response entity for request: " + this.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(numberOfProducts, HttpStatus.OK);
    }

    @PutMapping("/flower")
    public ResponseEntity<MessageResponseDTO> putFlowerOrder(@Valid @RequestParam("id") UUID id, Principal principal) {
        log.info(() -> "Processing request: " + this.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + id.toString());

        service.addFlowerToShoppingCart(principal.getName(), id);

        log.trace(() -> "Building response entity for request:" + this.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + id.toString());
        return null;
    }

    @PutMapping("/occasional-article")
    public ResponseEntity<MessageResponseDTO> putOccasionalArticleOrder(@Valid @RequestParam("id") UUID id, Principal principal) {
        log.info(() -> "Processing request: " + this.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + id.toString());

        log.trace(() -> "Building response entity for request: " + this.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + id.toString());
        return null;
    }

    @PutMapping("/souvenir")
    public ResponseEntity<MessageResponseDTO> putSouvenirOrder(@Valid @RequestParam("id") UUID id, Principal principal) {
        log.info(() -> "Processing request: " + this.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + id.toString());

        log.trace(() -> "Building response entity for request: " + this.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + id.toString());
        return null;
    }

}


