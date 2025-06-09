package com.netshoes.wishlist.app.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.domain.exceptions.WishlistNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoveProductFromWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    public void execute(final String customerId, final String productId) {
        final Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));

        final Wishlist wishlistUpdate = wishlist.removeProduct(productId);

        if (wishlistUpdate.isEmpty()) {
            wishlistRepository.deleteById(wishlist.getId());
            log.info("Wishlist do cliente {} está vazia após remoção do produto {}", customerId, productId);
        } else {
            wishlistRepository.save(wishlistUpdate);
            log.info("Produto {} removido da wishlist do cliente {}", productId, customerId);
        }
    }
}
