package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.FlowerOrder;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.order.SouvenirOrder;
import com.dknapik.flowershop.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final AccountService accountService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository repository,
                               AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }


    /**
     * Retrieves shopping cart for provided account. If shopping cart was not found,
     * new entity is created, assigned to provided account and returned.
     *
     * @param accountName - From which account retrieve ShoppingCart
     * @return - Detailed shopping cart dto
     */
    public ShoppingCart retrieveSingleShoppingCart(String accountName) {
        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart shoppingCart = account.getShoppingCart();

        /* If shopping cart doesn't exists, create new instance to provided account */
        if (shoppingCart == null) {
            shoppingCart = createNewShoppingCart(accountName);
        }

        return shoppingCart;
    }

    /**
     * Create new Shopping Cart entity and assign it to account
     *
     * @param accountName - Which account assign new shopping cart to
     * @return - Newly assigned Shopping Cart entity
     */
    public ShoppingCart createNewShoppingCart(String accountName) {
        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart shoppingCart = new ShoppingCart();
        account.setShoppingCart(shoppingCart);

        accountService.updateAccount(account);

        return shoppingCart;
    }

    /**
     * Counts total number of currently added products to the shopping cart
     *
     * @param shoppingCartId - UUID of shopping cart
     * @return integer with number of products in shopping cart
     */
    public int countNumberOfProducts(UUID shoppingCartId) {
        Optional<ShoppingCart> searchResults = repository.findById(shoppingCartId);
        int numberOfProducts = 0;

        /* Check if shopping cart exists, and return 0 if it doesn't */
        if (!searchResults.isPresent()) {
            return numberOfProducts;
        }
        ShoppingCart shoppingCart = searchResults.get();

        /* Add number of orders for each product */
        if (shoppingCart.getBouquetList() != null) {
            numberOfProducts += shoppingCart.getBouquetList().size();
        }
        if (shoppingCart.getFlowerOrderList() != null) {
            numberOfProducts += shoppingCart.getFlowerOrderList().stream().mapToInt(FlowerOrder::getItemCount).sum();
        }
        if (shoppingCart.getOccasionalArticleOrderList() != null) {
            numberOfProducts += shoppingCart.getOccasionalArticleOrderList()
                    .stream().mapToInt(OccasionalArticleOrder::getItemCount).sum();
        }
        if (shoppingCart.getSouvenirOrderList() != null) {
            numberOfProducts += shoppingCart.getSouvenirOrderList()
                    .stream().mapToInt(SouvenirOrder::getItemCount).sum();
        }

        return numberOfProducts;
    }

}
