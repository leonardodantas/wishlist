package com.netshoes.wishlist.domain.exceptions;

public class WishlistLimitReachedException extends RuntimeException {

    public WishlistLimitReachedException(final String customerId) {
        super("Wishlist atingida para o cliente com ID " + customerId);
    }
}
