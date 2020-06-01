package com.dknapik.flowershop.services.order;

import com.dknapik.flowershop.constants.ShoppingCartMessage;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
import com.dknapik.flowershop.model.productorder.ProductOrder;
import com.dknapik.flowershop.model.productorder.SouvenirOrder;
import com.dknapik.flowershop.services.AccountService;
import com.dknapik.flowershop.services.bouquet.BouquetService;
import com.dknapik.flowershop.services.product.FlowerService;
import com.dknapik.flowershop.services.product.OccasionalArticleService;
import com.dknapik.flowershop.services.product.SouvenirService;
import com.dknapik.flowershop.services.productorder.ProductOrderService;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/* TODO: This class is rather long, should it be refactored into multiple smaller ones ? - investigate */

@Service
@Log4j2
public final class ShoppingCartService {
    private final ShoppingCartRepository repository;
    private final AccountService accountService;
    private final FlowerService flowerService;
    private final OccasionalArticleService occasionalArticleService;
    private final SouvenirService souvenirService;
    private final ProductOrderService productOrderService;
    private final BouquetService bouquetService;
    private final MoneyUtils moneyUtils;


    @Autowired
    public ShoppingCartService(ShoppingCartRepository repository,
                               AccountService accountService,
                               FlowerService flowerService,
                               OccasionalArticleService occasionalArticleService,
                               SouvenirService souvenirService,
                               ProductOrderService productOrderService,
                               BouquetService bouquetService,
                               MoneyUtils moneyUtils) {
        this.repository = repository;
        this.accountService = accountService;
        this.flowerService = flowerService;
        this.occasionalArticleService = occasionalArticleService;
        this.souvenirService = souvenirService;
        this.productOrderService = productOrderService;
        this.bouquetService = bouquetService;
        this.moneyUtils = moneyUtils;
    }


    /**
     * Retrieves shopping cart for provided account. If shopping cart was not found,
     * new entity is created, assigned to provided account and returned.
     *
     * @param accountName - From which account retrieve ShoppingCart
     * @return - Detailed shopping cart dto
     */
    public ShoppingCart retrieveSingleShoppingCart(String accountName) {
        log.traceEntry();

        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart shoppingCart = account.getShoppingCart();

        /* If shopping cart doesn't exists, create new instance to provided account */
        if (shoppingCart == null) {
            log.warn(() -> "Shopping cart for provided user " + accountName + " doesn't exists, creating new one");
            shoppingCart = createNewShoppingCart(accountName);
        }

        log.traceExit();
        return shoppingCart;
    }

    /**
     * Retrieves shopping cart for provided ID. If shopping cart was not found an ResourceNotFoundException is thrown.
     *
     * @param shoppingCartID - ID of shopping cart to retrieve
     * @return - Shopping cart with provided ID.
     */
    public ShoppingCart retrieveSingleShoppingCart(UUID shoppingCartID) {
        log.traceEntry();

        Optional<ShoppingCart> shoppingCartOptional = repository.findById(shoppingCartID);

        /* Check if it was even possible to retrieve this shopping cart by ID */
        if (!shoppingCartOptional.isPresent()) {
            log.throwing(Level.ERROR, new ResourceNotFoundException(ShoppingCartMessage.SHOPPING_CART_NOT_FOUND));
            throw new ResourceNotFoundException(ShoppingCartMessage.SHOPPING_CART_NOT_FOUND);
        }

        ShoppingCart shoppingCart = shoppingCartOptional.get();

        log.traceExit();
        return shoppingCart;
    }

    /**
     * Create new Shopping Cart entity and assign it to account
     *
     * @param accountName - Which account assign new shopping cart to
     * @return - Newly assigned Shopping Cart entity
     */
    public ShoppingCart createNewShoppingCart(String accountName) {
        log.traceEntry();

        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart shoppingCart = new ShoppingCart();
        account.setShoppingCart(shoppingCart);

        accountService.updateAccount(account);

        log.traceExit();
        return shoppingCart;
    }

    /**
     * Counts total number of currently added products to the shopping cart
     *
     * @param shoppingCartId - UUID of shopping cart
     * @return integer with number of products in shopping cart
     */
    public int countNumberOfProducts(UUID shoppingCartId) {
        log.traceEntry();

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
        log.traceExit();
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
        log.traceEntry();

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
        log.traceExit();
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
        log.traceEntry();

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

        log.traceExit();
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
        log.traceEntry();

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

        log.traceExit();
    }

    /**
     * Retrieves bouquet with provided id from database and adds it to the shopping cart associated with provided
     * account name. Bouquets unlike other products, doesn't contain special entities defining how many
     * numbers of the same bouquet are ordered. This is due to the fact that bouquets are highly modifiable and
     * it is expected for user to create his own bouquet composition rather than buy large collection of the same
     * bouquet type.
     *
     * @param accountName - account to modify shopping cart from.
     * @param bouquetID   - ID of the product that will be added to shopping cart
     */
    public void addBouquetToShoppingCart(String accountName, UUID bouquetID) {
        log.traceEntry();

        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Adding Bouquet to following shopping cart: " + shoppingCart.toString());

        /* Check if collection for provided products even exists, and initialize it if not */
        if (shoppingCart.getBouquetList() == null) {
            log.trace("Shopping cart doesn't contain any Bouquet collection, initializing...");
            shoppingCart.setBouquetList(new LinkedList<>());
        }

        shoppingCart.getBouquetList().add(bouquetService.retrieveBouquet(bouquetID));
        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);

        log.traceExit();
    }

    /**
     * Retrieves shopping cart for provided account, searches for provided product order inside mentioned shopping cart
     * and removes it from both shopping cart and database, regardless of number of products inside it.
     *
     * @param accountName   - account to retrieve shopping cart from
     * @param flowerOrderID - product ORDER id to remove from shopping cart
     */
    public void removeFlowerOrderFromShoppingCart(String accountName, UUID flowerOrderID) {
        log.traceEntry();

        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Removing Flower Order from following shopping cart: " + shoppingCart.toString());

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

        log.traceExit();
    }

    /**
     * Retrieves shopping cart for provided account, searches for provided product order inside mentioned shopping cart
     * and removes it from both shopping cart and database, regardless of number of products inside it.
     *
     * @param accountName              - account to retrieve shopping cart from
     * @param occasionalArticleOrderID - product ORDER id to remove from shopping cart
     */
    public void removeOccasionalArticleFromShoppingCart(String accountName, UUID occasionalArticleOrderID) {
        log.traceEntry();

        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Removing Occasional Article order from following shopping cart: " + shoppingCart.toString());

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
        log.traceExit();
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
        log.traceEntry();

        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Removing Souvenir order from following shopping cart: " + shoppingCart.toString());

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
        log.traceExit();
        repository.saveAndFlush(shoppingCart);
    }


    /**
     * Searches shopping cart retrieved from provided account, for provided bouquet id and removes it.
     *
     * @param accountName - account to retrieve shopping cart from
     * @param bouquetID   - id of the bouquet to remove from shopping cart
     */
    public void removeBouquetFromShoppingCart(String accountName, UUID bouquetID) {
        log.traceEntry();

        ShoppingCart shoppingCart = retrieveSingleShoppingCart(accountName);
        log.debug(() -> "Removing Bouquet from following shopping cart: " + shoppingCart.toString());

        /* Search shopping cart for provided Bouquet and delete it if it exists  */
        for (int i = 0; i < shoppingCart.getOccasionalArticleOrderList().size(); i++) {
            if (shoppingCart.getBouquetList().get(i).getId().equals(bouquetID)) {
                shoppingCart.getBouquetList().remove(i);
                break;
            }
        }

        log.debug(() -> "Saving following shopping cart: " + shoppingCart.toString());
        repository.saveAndFlush(shoppingCart);


        log.traceExit();
    }

    /**
     * Create new shopping cart and set it to provided account. Then remove the old shopping cart
     *
     * @param accountName - account login in which following actions should be performed.
     */
    public void clearShoppingCart(@NonNull String accountName) {
        log.traceEntry();

        /* Retrieve shopping cart  */
        log.debug(() -> "Clearing shopping cart");
        Account account = accountService.retrieveAccountByName(accountName);
        ShoppingCart oldShoppingCart = account.getShoppingCart();

        /* Create and set new, empty shopping cart and save it to database */
        account.setShoppingCart(new ShoppingCart());
        accountService.updateAccount(account);

        /* Remove old shopping cart */
        repository.delete(oldShoppingCart);
        repository.flush();

        log.traceExit();
    }

    /**
     * Checks if provided shopping cart contains any Order entities inside it.
     *
     * @param shoppingCart - Shopping cart Entity to check
     * @return true if Shopping Cart is empty
     */
    public boolean isEmpty(ShoppingCart shoppingCart) {
        if (shoppingCart.getFlowerOrderList() != null && !shoppingCart.getFlowerOrderList().isEmpty()) {
            return false;
        }

        if (shoppingCart.getOccasionalArticleOrderList() != null &&
                !shoppingCart.getOccasionalArticleOrderList().isEmpty()) {
            return false;
        }

        if (shoppingCart.getSouvenirOrderList() != null && !shoppingCart.getSouvenirOrderList().isEmpty()) {
            return false;
        }

        return shoppingCart.getBouquetList() == null || shoppingCart.getBouquetList().isEmpty();
    }

    /**
     * Sum up price of all the Product Order entities that are nested inside provided Shopping Cart ID.
     * This method additionally checks if all of Product entities contain the same Currency and throws exception if not.
     *
     * @param shoppingCartID - Shopping Cart to count total price from.
     * @return MonetaryAmount containing equal currencies and summed up product prices or 0 with current application
     * currency
     */
    public MonetaryAmount countTotalPrice(UUID shoppingCartID) {
        log.traceEntry();
        ShoppingCart shoppingCart = retrieveSingleShoppingCart(shoppingCartID);
        List<List<? extends ProductOrder>> allProductOrders = shoppingCart.getAllProductOrders();

        /* Initialize total price starting with total price of all the bouquets inside provided order */
        MonetaryAmount totalPrice = null;
        MonetaryAmount orderListPriceSum;
        for (List<? extends ProductOrder> productOrdersList : allProductOrders) {

            /* Count total price for product order  */
            orderListPriceSum = productOrderService.countTotalCollectionPrice(productOrdersList);

            /* Check if total price is zero, if true there is no point in performing calculations */
            if (!orderListPriceSum.isZero()) {

                /* Check if return variable was initialized as it needs to have Currency Unit assigned from extracted
                 * product order sum instead of the whole application if we would like to introduce multiple
                 * currency unit support in the future.  */
                if (totalPrice == null) {
                    totalPrice = orderListPriceSum;
                } else {
                    if (!moneyUtils.checkMatchingCurrencies(totalPrice.getCurrency(),
                            orderListPriceSum.getCurrency())) {
                        log.throwing(new InvalidOperationException(ShoppingCartMessage.ERROR_MATCHING_CURRENCY_UNITS));
                        throw new InvalidOperationException(ShoppingCartMessage.ERROR_MATCHING_CURRENCY_UNITS);
                    }

                    /* If everything else is fine, add counted price to total price */
                    totalPrice = totalPrice.add(orderListPriceSum);
                }
            }
        }

        /* Add total bouquet price */
        MonetaryAmount totalBouquetPrice = bouquetService.countBouquetIterableTotalPrice(shoppingCart.getBouquetList());
        if (!totalBouquetPrice.isZero() && totalPrice != null) {
            totalPrice = totalPrice.add(totalBouquetPrice);

        }

        /* In order to not to return null, we check if above calculation even initialized return variable.
         *  If not, we initialize it with 0 value and application currently used currency. */
        if (totalPrice == null) {
            totalPrice = moneyUtils.zeroWithApplicationCurrency();
        }

        log.traceExit();
        return totalPrice;
    }

}
