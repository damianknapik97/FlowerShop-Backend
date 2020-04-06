package com.dknapik.flowershop.mapper;

import com.dknapik.flowershop.dto.product.FlowerDTO;
import com.dknapik.flowershop.dto.product.OccasionalArticleDTO;
import com.dknapik.flowershop.dto.product.ProductDTO;
import com.dknapik.flowershop.dto.product.SouvenirDTO;
import com.dknapik.flowershop.dto.productorder.ProductOrderDTO;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.model.product.Product;
import com.dknapik.flowershop.model.product.Souvenir;
import com.dknapik.flowershop.model.productorder.ProductOrder;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@ToString
@Log4j2
public final class ProductOrderMapper {
    private final ModelMapper modelMapper;
    private final ProductMapper productMapper;

    @Autowired
    public ProductOrderMapper(ModelMapper modelMapper, ProductMapper productMapper) {
        this.modelMapper = modelMapper;
        this.productMapper = productMapper;
    }

    /**
     * Convert Product Order Entity to Product Order Dto using Model Mapper
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    public <T extends ProductOrderDTO> T convertToDto(ProductOrder entity, Class<T> type) {
        log.traceEntry(() -> "Mapping " + entity.getProductOrderClass().getSimpleName() +
                " to " + type.getSimpleName());
        T productOrderDTO = type.cast(modelMapper.map(entity, type));

        /* Map product to product DTO because model mapper doesn't handle this correctly */
        productOrderDTO.setProductDTO(convertProductToDTO(entity.getProduct()));

        log.traceExit();
        return productOrderDTO;
    }

    /**
     * Convert Product Order Dto to Product Order Entity using Model Mapper
     *
     * @param productOrderDto - dto to map to entity
     * @return - mapped entity
     */
    public <T extends ProductOrder> T convertToEntity(ProductOrderDTO productOrderDto, Class<T> type) {
        log.traceEntry(() -> "Mapping " + productOrderDto.getProductOrderDTOClass().getSimpleName() +
                " to " + type.getSimpleName());
        T productOrder = type.cast(modelMapper.map(productOrderDto, type));

        productOrder.setProduct(convertProductDTOToEntity(productOrderDto.getProductDTO()));

        log.traceExit();
        return productOrder;
    }

    /**
     * Converts Product entity from within the Product Order cause model mapper sometimes have troubles with this.
     */
    private ProductDTO convertProductToDTO(Product product) {
        log.traceEntry();
        Product toConvert = Objects.requireNonNull(product);
        ProductDTO mappedProduct = null;

        /* Determine product class and perform mapping based on that */
        if (toConvert.compareClass(Flower.class)) {
            mappedProduct = productMapper.convertToDto(toConvert, FlowerDTO.class);
        } else if (toConvert.compareClass(OccasionalArticle.class)) {
            mappedProduct = productMapper.convertToDto(toConvert, OccasionalArticleDTO.class);
        } else if (toConvert.compareClass(Souvenir.class)) {
            mappedProduct = productMapper.convertToDto(toConvert, SouvenirDTO.class);
        }

        log.traceExit();
        return mappedProduct;
    }

    /**
     * Converts Product DTO  from within the Product Order DTO cause model mapper sometimes have troubles with this.
     */
    private Product convertProductDTOToEntity(ProductDTO productDTO) {
        log.traceEntry();
        ProductDTO toConvert = Objects.requireNonNull(productDTO);
        Product mappedProduct = null;

        /* Determine product class and perform mapping based on that */
        if (toConvert.compareClass(FlowerDTO.class)) {
            mappedProduct = productMapper.convertToEntity(toConvert, Flower.class);
        } else if (toConvert.compareClass(OccasionalArticleDTO.class)) {
            mappedProduct = productMapper.convertToEntity(toConvert, OccasionalArticle.class);
        } else if (toConvert.compareClass(SouvenirDTO.class)) {
            mappedProduct = productMapper.convertToEntity(toConvert, Souvenir.class);
        }

        log.traceExit();
        return mappedProduct;
    }

}
