package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.OccasionalArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OccasionalArticleRepository extends JpaRepository<OccasionalArticle, UUID> {

    Optional<OccasionalArticle> findByNameAndDescriptionAndTheme(String name, String description, String theme);

}
