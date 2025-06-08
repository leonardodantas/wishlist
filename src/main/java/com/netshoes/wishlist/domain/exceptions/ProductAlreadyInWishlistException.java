package com.netshoes.wishlist.domain.exceptions;

public class ProductAlreadyInWishlistException extends RuntimeException {

    public ProductAlreadyInWishlistException(final String productId) {
        super("Produto com ID " + productId + " já está na lista de desejos.");
    }
}
