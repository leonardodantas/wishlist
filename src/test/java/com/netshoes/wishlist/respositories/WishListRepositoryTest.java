package com.netshoes.wishlist.respositories;

import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import com.netshoes.wishlist.infra.database.repositories.WishListMongoRepository;
import com.netshoes.wishlist.infra.database.repositories.WishListRepository;
import com.netshoes.wishlist.infra.mappers.WishlistMapper;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishListRepositoryTest {

    private static final String CUSTOMER_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";

    @Spy
    private WishlistMapper wishlistMapper = Mappers.getMapper(WishlistMapper.class);
    @Mock
    private WishListMongoRepository wishListMongoRepository;
    @InjectMocks
    private WishListRepository wishListRepository;

    @Test
    void shouldFindByCustomerId() {
        final WishlistDocument document = JsonMock.getWishlistDocument_1();
        when(wishListMongoRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Optional.of(document));

        final Optional<Wishlist> wishlist = wishListRepository.findByCustomerId(CUSTOMER_ID);

        assertTrue(wishlist.isPresent());
        verify(wishListMongoRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void shouldReturnOptionalEmptyWhereDocumentNotFound() {
        final Optional<Wishlist> wishlist = wishListRepository.findByCustomerId(CUSTOMER_ID);

        assertTrue(wishlist.isEmpty());
        verify(wishListMongoRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void shouldSaveWishlist() {
        final Wishlist wishlist = JsonMock.getWishlistValid_1();
        final WishlistDocument document = JsonMock.getWishlistDocumentExpected();
        when(wishlistMapper.toDocument(wishlist)).thenReturn(document);

        wishListRepository.save(wishlist);

        verify(wishListMongoRepository).save(document);
    }

    @Test
    void shouldDeleteById() {
        final String id = "e2f3d4c5-b6a7-8901-abcd-ef1234567890";

        wishListRepository.deleteById(id);

        verify(wishListMongoRepository).deleteById(id);
    }
}
