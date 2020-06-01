package com.dknapik.flowershop.services.bouquet;

import com.dknapik.flowershop.constants.BouquetMessage;
import com.dknapik.flowershop.constants.ProductMessage;
import com.dknapik.flowershop.constants.sorting.ProductSortingProperty;
import com.dknapik.flowershop.database.bouquet.BouquetRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.services.productorder.ProductOrderService;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import java.util.Optional;
import java.util.UUID;


@Service
@ToString
@Log4j2
public final class BouquetService {
    private final BouquetRepository bouquetRepository;
    private final ProductOrderService productOrderService;
    private final MoneyUtils moneyUtils;

    @Autowired
    public BouquetService(BouquetRepository bouquetRepository,
                          ProductOrderService productOrderService,
                          MoneyUtils moneyUtils) {
        this.bouquetRepository = bouquetRepository;
        this.productOrderService = productOrderService;
        this.moneyUtils = moneyUtils;
    }

    /**
     * This function counts total price of all the nested products inside provided bouquet object. If no products are
     * present, then MonetaryAmount containing number 0 and default application currency will be returned. Additionally
     * this function checks if all the nested products that are counted, contain the same currency code saved
     * in database and throws InvalidOperationException runtime exception otherwise. Discount percentage is included
     * with the final price returned by this method.
     *
     * @param bouquet - Object to count total price from
     * @return MonetaryAmount with sum of all nested products inside provided object.
     */
    public MonetaryAmount countBouquetPrice(Bouquet bouquet) {
        log.traceEntry();

        MonetaryAmount totalBouquetPrice =
                productOrderService.countTotalCollectionPrice(bouquet.getBouquetFlowerList());
        MonetaryAmount totalAddonPrice =
                productOrderService.countTotalCollectionPrice(bouquet.getBouquetAddonList());


        /* Check if both flower and addon prices are zero,
        if true, we return zero with default application currency code  */
        if (totalBouquetPrice.isZero() && totalAddonPrice.isZero()) {
            return moneyUtils.zeroWithApplicationCurrency();
        }

        /* Check if total price sum of flowers is zero, if true, we return addon price regardless of whats inside */
        if (totalBouquetPrice.isZero()) {
            /* Addon price is subtracted by discount percentage */
            return totalAddonPrice.multiply(((double) bouquet.getDiscountPercentage() / 100));
        }

        /* If total price sum of flowers wasn't zero, we check if total addon price is zero, if true we return
         * total sum of flowers, regardless of what is inside */
        if (totalAddonPrice.isZero()) {
            /* Flower price is subtracted by discount percentage */
            return totalBouquetPrice.multiply(((double) bouquet.getDiscountPercentage() / 100));
        }

        /* Both previous statements were false, it turns out that total flower and addon price aren't zero and
         * we have to sum them up. In order to accomplish this, we have to check if currencies inside those
         * two object match. */
        if (!moneyUtils.checkMatchingCurrencies(totalBouquetPrice.getCurrency(),
                totalAddonPrice.getCurrency())) {
            log.throwing(new InvalidOperationException((BouquetMessage.CURRENCIES_NOT_MATCHING)));
            throw new InvalidOperationException(BouquetMessage.CURRENCIES_NOT_MATCHING);
        }

        /* We add prices and return them. Total price is subtracted by discount percentage. */
        totalBouquetPrice = totalBouquetPrice.add(totalAddonPrice);
        totalBouquetPrice = totalBouquetPrice.multiply(((double) bouquet.getDiscountPercentage() / 100));

        log.traceExit();
        return totalBouquetPrice;
    }


    /**
     * Counts total sum of all the bouquet prices inside provided Iterable. If currency units doesn't match
     * between mentioned objects, an InvalidOperationException is thrown. If bouquet is null or it doesn't
     * contain any objects inside, MonetaryAmount containing zero and application currency unit is returned.
     *
     * @param bouquetIterable - iterable with bouquet objects
     * @return MonetaryAmount with sum of all the bouquets
     */
    public MonetaryAmount countBouquetIterableTotalPrice(Iterable<Bouquet> bouquetIterable) {
        log.traceEntry();

        /* Check if provided iterable is not null. */
        if (bouquetIterable == null) {
            return moneyUtils.zeroWithApplicationCurrency();
        }

        MonetaryAmount totalPrice = null;
        MonetaryAmount currentBouquetPrice;

        for (Bouquet bouquet : bouquetIterable) {
            /* Count total price for bouquet */
            currentBouquetPrice = countBouquetPrice(bouquet);

            /* Check if bouquet price is zero, if true there is no point in performing calculations */
            if (!currentBouquetPrice.isZero()) {

                /* Check if return variable was initialized as it needs to have Currency Unit assigned from extracted
                 * product entity instead of the whole application if we would like to introduce multiple
                 * currency unit support in the future.  */
                if (totalPrice == null) {
                    totalPrice = currentBouquetPrice;
                } else {

                    /* Check if currency of freshly calculated bouquet, matches the currency of the rest.
                     *  If not, we do not want to lose money by creating under priced total price
                     * so we throw exception*/
                    if (!moneyUtils.checkMatchingCurrencies(totalPrice.getCurrency(),
                            currentBouquetPrice.getCurrency())) {
                        log.throwing(new InvalidOperationException(BouquetMessage.CURRENCIES_NOT_MATCHING));
                        throw new InvalidOperationException(BouquetMessage.CURRENCIES_NOT_MATCHING);
                    }

                    /* If everything else is fine, add counted price to total price */
                    totalPrice = totalPrice.add(currentBouquetPrice);

                }
            }
        }

        /* In order to not to return null, we check if above calculation even initialized return variable.
         *  If not, we initialize it with 0 value and application currently used currency. */
        if (totalPrice == null) {
            totalPrice = moneyUtils.zeroWithApplicationCurrency();
        }

        log.traceExit();
        return totalPrice;
    }


    /**
     * Searches database for provided Biyqyet ID, and returns related to it entity.
     * After search for mentioned ID fails, ResourceNotFound runtime exception is thrown.
     */
    public Bouquet retrieveBouquet(UUID bouquetID) {
        log.traceEntry();

        Optional<Bouquet> bouquetOptional = bouquetRepository.findById(bouquetID);
        if (!bouquetOptional.isPresent()) {
            log.throwing(new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND));
            throw new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND);

        }

        log.traceExit();
        return bouquetOptional.get();
    }

    /**
     * Creates ResponsePage containing sorted Bouquet entities.
     *
     * @param pageNumber      - which page should be retrieved
     * @param pageSize        - how large should the page be.
     * @param sortingProperty - how orders should be ordered when retrieved from database
     * @return - RestResponsePage with Order entities.
     */
    public RestResponsePage<Bouquet> retrieveBouquetsPage(int pageNumber, int pageSize,
                                                          @NonNull ProductSortingProperty sortingProperty) {
        log.traceEntry();

        /* Create Page request for repository and retrieve it. */
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Bouquet> content = retrieveSortedPage(pageable, sortingProperty);

        /* Return collection of products ready for transport/serialization/mapping */
        log.traceExit();
        return new RestResponsePage<>(content);
    }

    /**
     * Determines which sorting method is desired from provided enum, calls according repository function containing
     * suitable query, and returns Page object containing Bouquet entities inside.
     * If no property matches currently defined ones in this function, default Spring sorting is used.
     *
     * @param pageable        - Page request used in repository for page retrieval
     * @param sortingProperty - Enum containing name of desired sorting method
     * @return Page with Bouquets
     */
    private Page<Bouquet> retrieveSortedPage(@NonNull Pageable pageable,
                                             @NonNull ProductSortingProperty sortingProperty) {
        log.traceEntry();

        Page<Bouquet> bouquetPage;
        switch (sortingProperty) {
            case NAME_ASCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByNameAsc(pageable);
                break;
            case NAME_DESCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByNameDesc(pageable);
                break;
            case PRICE_DESCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByProductionCostDesc(pageable);
                break;
            case PRICE_ASCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByProductionCostAsc(pageable);
                break;
            default:
                bouquetPage = bouquetRepository.findAll(pageable);
        }

        log.traceExit();
        return bouquetPage;
    }

}
