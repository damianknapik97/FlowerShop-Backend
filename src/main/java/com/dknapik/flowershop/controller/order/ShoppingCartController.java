package com.dknapik.flowershop.controller.order;

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
        log.info("Processing getShoppingCart request");
        ShoppingCart shoppingCart = service.retrieveSingleShoppingCart(principal.getName());
        if (shoppingCart.getOccasionalArticleOrderList() != null) {
            System.out.println(shoppingCart.getOccasionalArticleOrderList().size());
        }


        log.info("Casting retrieved results to dto");
        ShoppingCartDTO shoppingCartDto = mapper.convertToDTO(shoppingCart);
        System.out.println(shoppingCartDto.getOccasionalArticleOrderDTOs().size());


        log.info("Building response");
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
        log.info("Processing countShoppingCartProducts request");
        int numberOfProducts = service.countNumberOfProducts(id);

        log.info("Building response entity");
        return new ResponseEntity<>(numberOfProducts, HttpStatus.OK);
    }
}


