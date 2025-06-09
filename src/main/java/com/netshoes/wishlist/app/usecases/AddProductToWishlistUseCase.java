package com.netshoes.wishlist.app.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.domain.Product;
import com.netshoes.wishlist.domain.Wishlist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
                            log.info("Produto adicionado à wishlist do cliente: {}", customerId);
                        },
                        () -> {
                            final Wishlist wishlist = Wishlist.of(customerId, product);
                            wishlistRepository.save(wishlist);
                            log.info("Wishlist criada e produto adicionado para o cliente: {}", customerId);
                        }
                );

    }


}
