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

/* TODO: This class is rather long, should it be refactored into multiple smaller ones ? - investigate */

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

    /**
     * Add or increment number of products from provided id inside product order
     * inside shopping cart for provided account.
     *
     * @param accountName - account to retrieve shopping cart from
     * @param flowerID    - product id to add to shopping cart
     */
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
            if (flowerOrder.getFlower().getId().equals(flowerID)) {
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

    /**
     * Add or increment number of products from provided id inside product order
     * inside shopping cart for provided account.
     *
     * @param accountName         - account to retrieve shopping cart from
     * @param occasionalArticleID - product id to add to shopping cart
     */
    public void addOccasionalArticleToShoppingCart(String accountName, UUID occasionalArticleID) {
        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Adding flower to following shopping cart: " + shoppingCart.toString());

        /* Check if collection for provided products even exists, and initialize it if not */
        if (shoppingCart.getOccasionalArticleOrderList() == null) {
            log.trace("Shopping cart doesn't contain any Occasional Article collection, initializing...");
            shoppingCart.setOccasionalArticleOrderList(new LinkedList<>());
        }

        /* Check if order with provided product already exists, and increase it quantity if true */
        boolean orderAlreadyExists = false;
        for (OccasionalArticleOrder occasionalArticleOrder : shoppingCart.getOccasionalArticleOrderList()) {
            if (occasionalArticleOrder.getOccasionalArticle().getId().equals(occasionalArticleID)) {
                log.trace("Order with provided product already exists, incrementing number of products");
                occasionalArticleOrder.setItemCount(occasionalArticleOrder.getItemCount() + 1);
                orderAlreadyExists = true;
            }
        }

        /* If order doesn't exists, we create new one */
        if (!orderAlreadyExists) {
            log.trace("Order with provided product DOESN'T already exists, creating new one");
            shoppingCart.getOccasionalArticleOrderList().add(new OccasionalArticleOrder(
                    1, occasionalArticleService.retrieveSingleOccasionalArticle(occasionalArticleID)));
        }

        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);
    }

    /**
     * Add or increment number of products from provided id inside product order
     * inside shopping cart for provided account.
     *
     * @param accountName - account to retrieve shopping cart from
     * @param souvenirID  - product id to add to shopping cart
     */
    public void addSouvenirToShoppingCart(String accountName, UUID souvenirID) {
        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Adding flower to following shopping cart: " + shoppingCart.toString());

        /* Check if collection for provided products even exists, and initialize it if not */
        if (shoppingCart.getSouvenirOrderList() == null) {
            log.trace("Shopping cart doesn't contain any Souvenir collection, initializing...");
            shoppingCart.setSouvenirOrderList(new LinkedList<>());
        }

        /* Check if order with provided product already exists, and increase it quantity if true */
        boolean orderAlreadyExists = false;
        for (SouvenirOrder souvenirOrder : shoppingCart.getSouvenirOrderList()) {
            if (souvenirOrder.getSouvenir().getId().equals(souvenirID)) {
                log.trace("Order with provided product already exists, incrementing number of products");
                souvenirOrder.setItemCount(souvenirOrder.getItemCount() + 1);
                orderAlreadyExists = true;
            }
        }

        /* If order doesn't exists, we create new one */
        if (!orderAlreadyExists) {
            log.trace("Order with provided product DOESN'T already exists, creating new one");
            shoppingCart.getSouvenirOrderList().add(new SouvenirOrder(
                    1, souvenirService.retrieveSingleSouvenir(souvenirID)));
        }

        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);
    }

    /**
     * Retrieves shopping cart for provided account, searches for provided product order inside mentioned shopping cart
     * and removes it from both shopping cart and database, regardless of number of products inside it.
     *
     * @param accountName   - account to retrieve shopping cart from
     * @param flowerOrderID - product ORDER id to remove from shopping cart
     */
    public void removeFlowerOrderFromShoppingCart(String accountName, UUID flowerOrderID) {
        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Removing product order from following shopping cart: " + shoppingCart.toString());

        /* Check if collection for provided products even exists, and initialize it if not */
        if (shoppingCart.getFlowerOrderList() == null) {
            log.trace("Shopping cart doesn't contain any Flower collection, initializing...");
            shoppingCart.setFlowerOrderList(new LinkedList<>());
        }

        /* Search shopping cart for provided product order and delete it if it exists  */
        for (int i = 0; i < shoppingCart.getFlowerOrderList().size(); i++) {
            if (shoppingCart.getFlowerOrderList().get(i).getId().equals(flowerOrderID)) {
                shoppingCart.getFlowerOrderList().remove(i);
                break;
            }
        }

        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);
    }

    /**
     * Retrieves shopping cart for provided account, searches for provided product order inside mentioned shopping cart
     * and removes it from both shopping cart and database, regardless of number of products inside it.
     *
     * @param accountName              - account to retrieve shopping cart from
     * @param occasionalArticleOrderID - product ORDER id to remove from shopping cart
     */
    public void removeOccasionalArticleFromShoppingCart(String accountName, UUID occasionalArticleOrderID) {
        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Removing product order from following shopping cart: " + shoppingCart.toString());

        /* Check if collection for provided products even exists, and initialize it if not */
        if (shoppingCart.getOccasionalArticleOrderList() == null) {
            log.trace("Shopping cart doesn't contain any Occasional Article collection, initializing...");
            shoppingCart.setOccasionalArticleOrderList(new LinkedList<>());
        }

        /* Search shopping cart for provided product order and delete it if it exists  */
        for (int i = 0; i < shoppingCart.getOccasionalArticleOrderList().size(); i++) {
            if (shoppingCart.getOccasionalArticleOrderList().get(i).getId().equals(occasionalArticleOrderID)) {
                shoppingCart.getOccasionalArticleOrderList().remove(i);
                break;
            }
        }

        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);
    }

    /**
     * Retrieves shopping cart for provided account, searches for provided product order inside mentioned shopping cart
     * and removes it from both shopping cart and database, regardless of number of products inside it.
     *
     * @param accountName     - account to retrieve shopping cart from
     * @param souvenirOrderID - product ORDER id to remove from shopping cart
     */
    public void removeSouvenirArticleFromShoppingCart(String accountName, UUID souvenirOrderID) {
        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Removing product order from following shopping cart: " + shoppingCart.toString());

        /* Check if collection for provided products even exists, and initialize it if not */
        if (shoppingCart.getSouvenirOrderList() == null) {
            log.trace("Shopping cart doesn't contain any Souvenir collection, initializing...");
            shoppingCart.setSouvenirOrderList(new LinkedList<>());
        }

        /* Search shopping cart for provided product order and delete it if it exists  */
        for (int i = 0; i < shoppingCart.getOccasionalArticleOrderList().size(); i++) {
            if (shoppingCart.getOccasionalArticleOrderList().get(i).getId().equals(souvenirOrderID)) {
                shoppingCart.getOccasionalArticleOrderList().remove(i);
                break;
            }
        }

        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);
    }
}
