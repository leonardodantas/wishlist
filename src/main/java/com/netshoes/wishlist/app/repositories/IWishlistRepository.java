package com.netshoes.wishlist.app.repositories;

import com.netshoes.wishlist.domain.Wishlist;

import java.util.Optional;

public interface IWishlistRepository {
    Optional<Wishlist> findByCustomerId(final String customerId);

    void save(final Wishlist wishlist);
}
