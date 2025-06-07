package com.netshoes.wishlist.app.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.domain.exceptions.WishlistNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveProductFromWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    public void execute(final String customerId, final String productId) {
        final Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));

        final Wishlist wishlistUpdate = wishlist.removeProduct(productId);

        if (wishlistUpdate.isEmpty()) {
            wishlistRepository.deleteById(customerId);
        } else {
            wishlistRepository.save(wishlistUpdate);
        }
    }
}
