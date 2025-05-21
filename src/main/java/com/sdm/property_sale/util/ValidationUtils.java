package com.sdm.property_sale.util;

import com.sdm.property_sale.entity.User;
import com.sdm.property_sale.enums.UserType;
import com.sdm.property_sale.exception.ForbiddenException;
import com.sdm.property_sale.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {

    private final UserRepository userRepository;

    public ValidationUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateRegistrationPermissions(User currentUser, UserType targetUserType) {
        if (currentUser.getUserType() == UserType.USER) {
            throw new ForbiddenException("Users cannot register new accounts");
        }

        if (currentUser.getUserType() == UserType.ADMIN && targetUserType != UserType.USER) {
            throw new ForbiddenException("Admins can only register users");
        }

        if (currentUser.getUserType() == UserType.SUPER_ADMIN && targetUserType == UserType.SUPER_ADMIN) {
            throw new ForbiddenException("Super admins cannot register other super admins");
        }
    }

    public void validateUpdatePermissions(User currentUser, User targetUser) {
        if (currentUser.getUserType() == UserType.USER) {
            throw new ForbiddenException("Users cannot update accounts");
        }

        if (currentUser.getUserType() == UserType.ADMIN &&
                (targetUser.getUserType() == UserType.ADMIN || targetUser.getUserType() == UserType.SUPER_ADMIN)) {
            throw new ForbiddenException("Admins can only update user accounts");
        }

        if (currentUser.getUserType() == UserType.SUPER_ADMIN && targetUser.getUserType() == UserType.SUPER_ADMIN &&
                !currentUser.getId().equals(targetUser.getId())) {
            throw new ForbiddenException("Super admins cannot update other super admins");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));
    }

    public void checkForDuplicates(String username, String email, String mobile) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(Constants.DUPLICATE_USERNAME);
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(Constants.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByMobile(mobile)) {
            throw new IllegalArgumentException(Constants.DUPLICATE_MOBILE);
        }
    }
}
