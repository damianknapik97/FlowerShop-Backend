package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.OccasionalArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OccasionalArticleRepository extends JpaRepository<OccasionalArticle, UUID> {

    Optional<OccasionalArticle> findByNameAndDescriptionAndTheme(String name, String description, String theme);

}
