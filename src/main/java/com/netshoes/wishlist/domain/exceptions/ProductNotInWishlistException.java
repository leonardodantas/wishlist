package com.netshoes.wishlist.domain.exceptions;

public class ProductNotInWishlistException extends RuntimeException {

    public ProductNotInWishlistException(final String productId) {
        super("Produto com ID " + productId + " não está na lista de desejos.");
    }

}
