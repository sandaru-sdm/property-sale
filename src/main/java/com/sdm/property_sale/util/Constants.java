package com.sdm.property_sale.util;

public class Constants {
    public static final String AUTH_PATH = "/api/v1/auth";
    public static final String USERS_PATH = "/api/v1/users";

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SUPER_ADMIN_ROLE = "ROLE_SUPER_ADMIN";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String USER_ROLE = "ROLE_USER";

    // Validation error messages
    public static final String DUPLICATE_USERNAME = "Username already exists";
    public static final String DUPLICATE_EMAIL = "Email already exists";
    public static final String DUPLICATE_MOBILE = "Mobile number already exists";
    public static final String USER_NOT_FOUND = "User not found";
}
