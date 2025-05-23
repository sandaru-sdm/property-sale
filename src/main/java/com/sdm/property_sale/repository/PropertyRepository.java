package com.sdm.property_sale.repository;

import com.sdm.property_sale.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<Property> findByIdAndUserId(Long id, UUID userId);
    List<Property> findByUserIdOrderByCreatedAtDesc(UUID userId);
}