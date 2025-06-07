package com.netshoes.wishlist.app.usecases;

import com.netshoes.wishlist.domain.Product;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddProductToWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    public void execute(final String customerId, final Product product) {
        wishlistRepository.findByCustomerId(customerId)
                .ifPresentOrElse(
                        wishlist -> {
                            final Wishlist wishlistUpdate = wishlist.addProduct(product);
                            wishlistRepository.save(wishlistUpdate);
                        },
                        () -> {
                            final Wishlist wishlist = Wishlist.of(customerId, product);
                            wishlistRepository.save(wishlist);
                        }
                );

    }


}
