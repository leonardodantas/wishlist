package com.netshoes.wishlist.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.app.usecases.ProductExistsInWishlistUseCase;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.domain.exceptions.WishlistNotFoundException;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductExistsInWishlistUseCaseTest {

    private static final String CUSTOMER_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";
    private static final String PRODUCT_ID = "87332cf1-c4de-4a59-9aaf-1ad9db387bd8";

    @Mock
    private IWishlistRepository wishlistRepository;

    @InjectMocks
    private ProductExistsInWishlistUseCase productExistsInWishlistUseCase;

    @Test
    void shouldReturnTrueWhenProductExist() {
        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        final Boolean productExist = productExistsInWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID);

        assertTrue(productExist);
        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void shouldReturnFalseWhenProductNotFound() {
        final Wishlist wishlist = JsonMock.getWishlistWith19Products();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        final Boolean productExist = productExistsInWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID);

        assertFalse(productExist);
        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void shouldThrowWishlistNotFoundExceptionWhenWishlistNotFound() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(WishlistNotFoundException.class, () -> productExistsInWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID));

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
    }
}
