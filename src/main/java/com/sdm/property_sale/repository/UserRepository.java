package com.sdm.property_sale.repository;

import com.sdm.property_sale.entity.User;
import com.sdm.property_sale.enums.Status;
import com.sdm.property_sale.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByMobile(String mobile);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByMobile(String mobile);
    List<User> findByUserType(UserType userType);
    List<User> findByUserTypeAndStatus(UserType userType, Status status);
}
