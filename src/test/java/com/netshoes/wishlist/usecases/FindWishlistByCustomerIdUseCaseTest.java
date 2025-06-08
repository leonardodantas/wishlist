package com.netshoes.wishlist.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.app.usecases.FindWishlistByCustomerIdUseCase;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindWishlistByCustomerIdUseCaseTest {

    private static final String CUSTOMER_ID = "84c8b0f-4d2e-4a1b-9c3f-5d6e7f8a9b0c";

    @Mock
    private IWishlistRepository wishlistRepository;

    @InjectMocks
    private FindWishlistByCustomerIdUseCase findWishlistByCustomerIdUseCase;

    @Test
    void shouldFindWishlistByCustomerId() {

        final Wishlist wishlistWith20Products = JsonMock.getWishlistWith20Products();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlistWith20Products));

        final Optional<Wishlist> wishlist = findWishlistByCustomerIdUseCase.execute(CUSTOMER_ID);

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        assertTrue(wishlist.isPresent());
    }

    @Test
    void shouldFindWishlistEmptyByCustomerId() {

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        final Optional<Wishlist> wishlist = findWishlistByCustomerIdUseCase.execute(CUSTOMER_ID);

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        assertTrue(wishlist.isEmpty());
    }
}
