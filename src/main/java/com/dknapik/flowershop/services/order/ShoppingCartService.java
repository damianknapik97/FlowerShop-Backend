package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.dto.order.ShoppingCartDetailedDto;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.services.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShoppingCartService {
    private final ModelMapper mapper = new ModelMapper();             // less messy dto - model mapping
    private final ShoppingCartRepository repository;
    private final AccountService accountService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository repository,
                               AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }


    /**
     *
     * @param accountName - From which account retrieve ShoppingCart
     * @return - Detailed shopping cart dto
     */
    public ShoppingCartDetailedDto retrieveSingleShoppingCart(String accountName) {
        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart shoppingCart = account.getShoppingCart();

        if (shoppingCart == null) {
            shoppingCart = createNewShoppingCart(accountName);
        }

        /* Map extracted ShoppingCart entity to DTO and return it */
        return mapper.map(shoppingCart, ShoppingCartDetailedDto.class);
    }

    /**
     *  Create new Shopping Cart entity and assign it to account
     *
     * @param accountName - Which account assign new shopping cart to
     * @return - Newly assigned Shopping Cart entity
     */
    public ShoppingCart createNewShoppingCart(String accountName) {
        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart shoppingCart = new ShoppingCart();
        account.setShoppingCart(shoppingCart);

        repository.saveAndFlush(shoppingCart);
        accountService.updateAccount(account);

        return shoppingCart;
    }

    public int countNumberOfProducts(String accountName) {
        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart shoppingCart = account.getShoppingCart();

        if (shoppingCart == null) {
            shoppingCart = createNewShoppingCart(accountName);
        }



        return 0;
    }
}
