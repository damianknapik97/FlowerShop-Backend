package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.FlowerOrder;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.order.SouvenirOrder;
import com.dknapik.flowershop.services.AccountService;
import com.dknapik.flowershop.services.product.FlowerService;
import com.dknapik.flowershop.services.product.OccasionalArticleService;
import com.dknapik.flowershop.services.product.SouvenirService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;


@Service
@Log4j2
public class ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final AccountService accountService;
    private final FlowerService flowerService;
    private final OccasionalArticleService occasionalArticleService;
    private final SouvenirService souvenirService;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository repository,
                               AccountService accountService,
                               FlowerService flowerService,
                               OccasionalArticleService occasionalArticleService,
                               SouvenirService souvenirService) {
        this.repository = repository;
        this.accountService = accountService;
        this.flowerService = flowerService;
        this.occasionalArticleService = occasionalArticleService;
        this.souvenirService = souvenirService;
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
            log.warn(() -> "Shopping cart for provided user " + accountName + " doesn't exists, creating new one");
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
        log.debug(() -> "Counting number of products in shopping cart with following id: " + shoppingCartId.toString());
        Optional<ShoppingCart> searchResults = repository.findById(shoppingCartId);
        int numberOfProducts = 0;

        /* Check if shopping cart exists, and return 0 if it doesn't */
        if (!searchResults.isPresent()) {
            log.warn(() -> "Searched shopping cart with following id doesn't exist: " + shoppingCartId.toString());
            return numberOfProducts;
        }
        ShoppingCart shoppingCart = searchResults.get();
        log.debug(() -> "Counting number of products in following shopping cart" + shoppingCart.toString());

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

        log.debug("Counted following number of products: ", numberOfProducts);
        return numberOfProducts;
    }

    public void addFlowerToShoppingCart(String accountName, UUID flowerID) {
        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Adding flower to following shopping cart: " + shoppingCart.toString());

        /* Check if collection for provided products even exists, and initialize it if not */
        if (shoppingCart.getFlowerOrderList() == null) {
            log.trace("Shopping cart doesn't contain any FlowerOrder collection, initializing...");
            shoppingCart.setFlowerOrderList(new LinkedList<>());
        }

        /* Check if order with provided product already exists, and increase it quantity if true */
        boolean orderAlreadyExists = false;
        for (FlowerOrder flowerOrder : shoppingCart.getFlowerOrderList()) {
            if (flowerOrder.getFlower().getId().compareTo(flowerID) == 0) {
                log.trace("Order with provided product already exists, incrementing number of products");
                flowerOrder.setItemCount(flowerOrder.getItemCount() + 1);
                orderAlreadyExists = true;
            }
        }

        /* If order doesn't exists, we create new one */
        if (!orderAlreadyExists) {
            log.trace("Order with provided product DOESN'T already exists, creating new one");
            shoppingCart.getFlowerOrderList().add(
                    new FlowerOrder(1, flowerService.retrieveSingleFlower(flowerID)));
        }

        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);
    }


}
