package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.ShoppingCartMessage;
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
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " for following user " + principal.getName());

        ShoppingCart shoppingCart = service.retrieveSingleShoppingCart(principal.getName());

        log.trace("Casting retrieved results to dto");
        ShoppingCartDTO shoppingCartDto = mapper.convertToDTO(shoppingCart);

        log.debug(() -> "Building response entity for request: " + this.getClass().getEnclosingMethod().getName() +
                " with following details " + shoppingCartDto.toString());
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
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " using following id " + id.toString());

        int numberOfProducts = service.countNumberOfProducts(id);

        log.debug(() -> "Building response entity for request: " +
                new Object() {}.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(numberOfProducts, HttpStatus.OK);
    }

    /**
     * Creates or increments number of products inside flower order that is contained in current user shopping cart.
     *
     * @param flowerID - Flower id that will be put inside flower order
     * @param principal - Current user account
     * @return Response with message about operation results.
     */
    @PutMapping("/flower")
    public ResponseEntity<MessageResponseDTO> putFlowerOrder(@Valid @RequestParam("id") UUID flowerID,
                                                             Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + flowerID.toString());

        service.addFlowerToShoppingCart(principal.getName(), flowerID);

        log.trace(() -> "Building response entity for request:" +
                new Object() {}.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Creates or increments number of products inside occasional article order
     * that is contained in current user shopping cart.
     *
     * @param occasionalArticleID - id of product
     * @param principal - user that shopping cart shall be used
     * @return Response with message about operation results.
     */
    @PutMapping("/occasional-article")
    public ResponseEntity<MessageResponseDTO> putOccasionalArticleOrder(
            @Valid @RequestParam("id") UUID occasionalArticleID, Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + occasionalArticleID.toString());

        service.addOccasionalArticleToShoppingCart(principal.getName(), occasionalArticleID);

        log.trace(() -> "Building response entity for request: " +
                new Object() {}.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Creates or increments number of products inside souvenir order
     * that is contained in current user shopping cart.
     *
     * @param souvenirID - id of product
     * @param principal - user that shopping cart shall be used
     * @return Response with message about operation results
     */
    @PutMapping("/souvenir")
    public ResponseEntity<MessageResponseDTO> putSouvenirOrder(@Valid @RequestParam("id") UUID souvenirID,
                                                               Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + souvenirID.toString());

        service.addSouvenirToShoppingCart(principal.getName(), souvenirID);

        log.trace(() -> "Building response entity for request: " + this.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Deletes Flower Order with provided ID from shopping cart, regardless of number of products inside it.
     *
     * @param flowerOrderID - id of order
     * @param principal - user that shopping cart shall be used
     * @return Response with message about operation results
     */
    @DeleteMapping("/flower")
    public ResponseEntity<MessageResponseDTO> deleteFlowerOrder(@Valid @RequestParam("id") UUID flowerOrderID,
                                                                Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + flowerOrderID.toString());

        service.removeFlowerOrderFromShoppingCart(principal.getName(), flowerOrderID);

        log.trace(() -> "Building response entity for request: " +
                new Object() {}.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Deletes Occasional Article Order with provided ID from shopping cart, regardless of number of products inside it.
     *
     * @param occasionalArticleOrderID - id of order
     * @param principal - user that shopping cart shall be used
     * @return Response with message about operation results
     */
    @DeleteMapping("/occasional-article")
    public ResponseEntity<MessageResponseDTO> deleteOccasionalArticleOrder(
            @Valid @RequestParam("id") UUID occasionalArticleOrderID, Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() +
                " - using following id " + occasionalArticleOrderID.toString());

        service.removeOccasionalArticleFromShoppingCart(principal.getName(), occasionalArticleOrderID);

        log.trace(() -> "Building response entity for request: " +
                new Object() {}.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     *
     * @param souvenirID
     * @param principal
     * @return
     */
    @DeleteMapping("/souvenir")
    public ResponseEntity<MessageResponseDTO> deleteSouvenirOrder(@Valid @RequestParam("id") UUID souvenirID,
                                                                  Principal principal) {
        log.info(() -> "Processing request: " + new Object() {}.getClass().getEnclosingMethod().getName() +
                " for following user - " + principal.getName() + " - using following id " + souvenirID.toString());

        service.removeSouvenirArticleFromShoppingCart(principal.getName(), souvenirID);

        log.trace(() -> "Building response entity for request: " +
                new Object() {}.getClass().getEnclosingMethod().getName());
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY), HttpStatus.OK);
    }

}


