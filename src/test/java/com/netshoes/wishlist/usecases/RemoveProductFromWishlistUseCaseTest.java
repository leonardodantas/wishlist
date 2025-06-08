package com.netshoes.wishlist.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.app.usecases.RemoveProductFromWishlistUseCase;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.domain.exceptions.ProductNotInWishlistException;
import com.netshoes.wishlist.domain.exceptions.WishlistNotFoundException;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveProductFromWishlistUseCaseTest {

    private static final String CUSTOMER_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";
    private static final String PRODUCT_ID = "87332cf1-c4de-4a59-9aaf-1ad9db387bd8";

    @Captor
    private ArgumentCaptor<Wishlist> wishlistCaptor;

    @Mock
    private IWishlistRepository wishlistRepository;

    @InjectMocks
    private RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @Test
    void shouldThrowsWishlistNotFoundExceptionWhereWishlistNotFound() {
        assertThrows(WishlistNotFoundException.class, () -> removeProductFromWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID));

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
        verify(wishlistRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldThrowsProductNotInWishlistExceptionWhereWishlistNotFound() {

        final Wishlist wishlist = JsonMock.getWishlistWith19Products();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        assertThrows(ProductNotInWishlistException.class, () -> removeProductFromWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID));

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
        verify(wishlistRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldThrowsProductNotInWishlistExceptionWhereWishlistWithoutProducts() {

        final Wishlist wishlist = JsonMock.getWishlistWithoutProducts();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        assertThrows(ProductNotInWishlistException.class, () -> removeProductFromWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID));

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
        verify(wishlistRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldRemoveProductWishlist() {

        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        removeProductFromWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID);

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository, never()).deleteById(anyString());

        verify(wishlistRepository).save(wishlistCaptor.capture());
        final Wishlist wishlistSave = wishlistCaptor.getValue();
        assertFalse(wishlistSave.containsProduct(PRODUCT_ID));
        assertEquals(19, wishlistSave.getProducts().size());
    }

    @Test
    void shouldRemoveProductAndDeleteWishlist() {

        final Wishlist wishlist = JsonMock.getWishlistWith1Products();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        removeProductFromWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID);

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository, never()).save(wishlistCaptor.capture());

        verify(wishlistRepository).deleteById(wishlist.getId());
    }
}
