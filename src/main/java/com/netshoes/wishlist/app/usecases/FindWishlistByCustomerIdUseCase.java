package com.netshoes.wishlist.app.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.domain.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindWishlistByCustomerIdUseCase {

    private final IWishlistRepository wishlistRepository;

    public Optional<Wishlist> execute(final String customerId) {
        return wishlistRepository.findByCustomerId(customerId);
    }
}
