package com.netshoes.wishlist.domain.exceptions;

public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException(final String customerId) {
        super("Lista de desejos não encontrada para o cliente com ID " + customerId + ".");
    }
}
