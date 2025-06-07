package com.netshoes.wishlist.domain.exceptions;

public class ProductAlreadyInWishlistException extends RuntimeException {

    public ProductAlreadyInWishlistException(final String productId) {
        super("Product with ID " + productId + " is already in the wishlist.");
    }
}
