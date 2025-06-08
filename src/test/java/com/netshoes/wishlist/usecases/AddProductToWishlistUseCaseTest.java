package com.netshoes.wishlist.usecases;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.app.usecases.AddProductToWishlistUseCase;
import com.netshoes.wishlist.domain.Product;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.domain.exceptions.ProductAlreadyInWishlistException;
import com.netshoes.wishlist.domain.exceptions.WishlistLimitReachedException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductToWishlistUseCaseTest {

    private static final String CUSTOMER_ID = "84c8b0f-4d2e-4a1b-9c3f-5d6e7f8a9b0c";

    @Captor
    private ArgumentCaptor<Wishlist> wishlistArgumentCaptor;

    @Mock
    private IWishlistRepository wishlistRepository;

    @InjectMocks
    private AddProductToWishlistUseCase addProductToWishlistUseCase;

    @Test
    void shouldSaveProductFirst() {
        final Product product = JsonMock.getProductValid_1();
        addProductToWishlistUseCase.execute(CUSTOMER_ID, product);

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository).save(wishlistArgumentCaptor.capture());

        final Wishlist wishlistSave = wishlistArgumentCaptor.getValue();
        assertEquals(1, wishlistSave.getProducts().size());
        assertTrue(wishlistSave.containsProduct(product));
    }

    @Test
    void shouldAddProductToExistingWishlist() {

        final Wishlist wishlist = JsonMock.getWishlistValid_1();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(wishlist));

        final Product product = JsonMock.getProductValid_1();
        addProductToWishlistUseCase.execute(CUSTOMER_ID, product);

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository).save(wishlistArgumentCaptor.capture());

        final Wishlist wishlistSave = wishlistArgumentCaptor.getValue();
        assertEquals(3, wishlistSave.getProducts().size());
        assertTrue(wishlistSave.containsProduct(product));
    }

    @Test
    void shouldThrowProductAlreadyInWishlistExceptionWhereProductAlreadyExist() {

        final Wishlist wishlist = JsonMock.getWishlistValid_2();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(wishlist));

        final Product product = JsonMock.getProductValid_1();

        assertThrows(ProductAlreadyInWishlistException.class, () -> addProductToWishlistUseCase.execute(CUSTOMER_ID, product));

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    void shouldThrowWishlistLimitReachedExceptionWhereLimitExceeded() {

        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(wishlist));

        final Product product = JsonMock.getProductValid_1();

        assertThrows(WishlistLimitReachedException.class, () -> addProductToWishlistUseCase.execute(CUSTOMER_ID, product));

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

}
