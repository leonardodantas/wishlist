package com.netshoes.wishlist.app.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.domain.exceptions.WishlistNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductExistsInWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    public Boolean execute(final String customerId, final String productId) {
        final Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
        return wishlist.containsProduct(productId);
    }
}
