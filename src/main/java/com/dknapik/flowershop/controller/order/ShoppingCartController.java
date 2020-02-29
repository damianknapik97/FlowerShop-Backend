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
final class ShoppingCartController {
    private final ShoppingCartService service;
    private final ShoppingCartMapper mapper;

    @Autowired
    ShoppingCartController(ShoppingCartService service, ShoppingCartMapper mapper) {
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
    ResponseEntity<ShoppingCartDTO> getShoppingCart(Principal principal) {
        log.traceEntry();

        ShoppingCart shoppingCart = service.retrieveSingleShoppingCart(principal.getName());
        log.trace("Casting retrieved results to dto");
        ShoppingCartDTO shoppingCartDto = mapper.convertToDTO(shoppingCart);

        log.traceExit();
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }

    /**
     * Counts number of products that are currently inside provided shopping cart id;
     *
     * @param id - UUID of shopping cart
     * @return - number of products inside shopping cart
     */
    @GetMapping("/count")
    ResponseEntity<Integer> countShoppingCartProducts(@Valid @RequestParam("id") UUID id) {
        log.traceEntry();

        int numberOfProducts = service.countNumberOfProducts(id);

        log.debug(() -> "Building response entity for request: " +
                new Object() {
                }.getClass().getEnclosingMethod().getName());
        log.traceExit();
        return new ResponseEntity<>(numberOfProducts, HttpStatus.OK);
    }

    /**
     * Creates or increments number of products inside flower order that is contained in current user shopping cart.
     *
     * @param flowerID  - Flower id that will be put inside flower order
     * @param principal - Current user account
     * @return Response with message about operation results.
     */
    @PutMapping("/flower")
    ResponseEntity<MessageResponseDTO> putFlowerOrder(@Valid @RequestParam("id") UUID flowerID,
                                                      Principal principal) {
        log.traceEntry();
        log.debug(() -> "Processing request for following user - " + principal.getName() +
                " - using following id " + flowerID.toString());

        service.addFlowerToShoppingCart(principal.getName(), flowerID);


        log.traceExit();
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Creates or increments number of products inside occasional article order
     * that is contained in current user shopping cart.
     *
     * @param occasionalArticleID - id of product
     * @param principal           - user that shopping cart shall be used
     * @return Response with message about operation results.
     */
    @PutMapping("/occasional-article")
    ResponseEntity<MessageResponseDTO> putOccasionalArticleOrder(
            @Valid @RequestParam("id") UUID occasionalArticleID, Principal principal) {
        log.traceEntry();
        log.debug(() -> "Processing request for following user - " +
                principal.getName() + " - using following id " + occasionalArticleID.toString());

        service.addOccasionalArticleToShoppingCart(principal.getName(), occasionalArticleID);


        log.traceExit();
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Creates or increments number of products inside souvenir order
     * that is contained in current user shopping cart.
     *
     * @param souvenirID - id of product
     * @param principal  - user that shopping cart shall be used
     * @return Response with message about operation results
     */
    @PutMapping("/souvenir")
    ResponseEntity<MessageResponseDTO> putSouvenirOrder(@Valid @RequestParam("id") UUID souvenirID,
                                                        Principal principal) {
        log.traceEntry();
        log.debug(() -> "Processing request for following user - " + principal.getName() +
                " - using following id " + souvenirID.toString());

        service.addSouvenirToShoppingCart(principal.getName(), souvenirID);

        log.traceExit();
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Deletes Flower Order with provided ID from shopping cart, regardless of number of products inside it.
     *
     * @param flowerOrderID - id of order
     * @param principal     - user that shopping cart shall be used
     * @return Response with message about operation results
     */
    @DeleteMapping("/flower")
    ResponseEntity<MessageResponseDTO> deleteFlowerOrder(@Valid @RequestParam("id") UUID flowerOrderID,
                                                         Principal principal) {
        log.traceEntry();
        log.debug(() -> "Processing request for following user - " + principal.getName() +
                " - using following id " + flowerOrderID.toString());

        service.removeFlowerOrderFromShoppingCart(principal.getName(), flowerOrderID);

        log.traceExit();
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Deletes Occasional Article Order with provided ID from shopping cart, regardless of number of products inside it.
     *
     * @param occasionalArticleOrderID - id of order
     * @param principal                - user that shopping cart shall be used
     * @return Response with message about operation results
     */
    @DeleteMapping("/occasional-article")
    ResponseEntity<MessageResponseDTO> deleteOccasionalArticleOrder(
            @Valid @RequestParam("id") UUID occasionalArticleOrderID, Principal principal) {
        log.traceEntry();
        log.debug(() -> "Processing request for following user - " + principal.getName() +
                " - using following id " + occasionalArticleOrderID.toString());

        service.removeOccasionalArticleFromShoppingCart(principal.getName(), occasionalArticleOrderID);

        log.traceExit();
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY), HttpStatus.OK);
    }

    /**
     * Deletes Souvenir Order with provided ID from shopping cart, regardless of number of products inside it.
     *
     * @param souvenirOrderID - id of order
     * @param principal       - user that shopping cart shall be used
     * @return Response with message about operation results
     */
    @DeleteMapping("/souvenir")
    ResponseEntity<MessageResponseDTO> deleteSouvenirOrder(@Valid @RequestParam("id") UUID souvenirOrderID,
                                                           Principal principal) {
        log.traceEntry();
        log.debug(() -> "Processing request for following user - " + principal.getName() +
                " - using following id " + souvenirOrderID.toString());

        service.removeSouvenirArticleFromShoppingCart(principal.getName(), souvenirOrderID);

        log.traceExit();
        return new ResponseEntity<>(
                new MessageResponseDTO(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY), HttpStatus.OK);
    }

}


