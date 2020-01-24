package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.dto.order.ShoppingCartDto;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.services.order.ShoppingCartService;
import org.modelmapper.ModelMapper;
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
public class ShoppingCartController {
    private final ShoppingCartService service;
    private final ModelMapper mapper;

    @Autowired
    public ShoppingCartController(ShoppingCartService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Returns detailed shopping cart instance, with all currently added productss.
     *
     * @param principal - logged user
     * @return ShoppingCart Dto with products, name and id
     */
    @GetMapping
    public ResponseEntity<ShoppingCartDto> getShoppingCart(Principal principal) {
        ShoppingCart shoppingCart = service.retrieveSingleShoppingCart(principal.getName());
        ShoppingCartDto shoppingCartDto = convertToDto(shoppingCart);

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
        int numberOfProducts = service.countNumberOfProducts(id);

        return new ResponseEntity<>(numberOfProducts, HttpStatus.OK);
    }

    /**
     * Maps provided object to entity.
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    private ShoppingCartDto convertToDto(ShoppingCart entity) {
        return mapper.map(entity, ShoppingCartDto.class);
    }

    /**
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    private ShoppingCart convertToEntity(ShoppingCartDto dto) {
        return mapper.map(dto, ShoppingCart.class);
    }
}


