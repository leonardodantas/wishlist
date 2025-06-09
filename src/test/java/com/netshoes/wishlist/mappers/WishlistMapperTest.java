package com.netshoes.wishlist.mappers;

import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import com.netshoes.wishlist.infra.http.jsons.responses.WishlistResponse;
import com.netshoes.wishlist.infra.mappers.WishlistMapper;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class WishlistMapperTest {

    private final WishlistMapper wishlistMapper = Mappers.getMapper(WishlistMapper.class);

    @Test
    void shouldMapWishlistToDocument() {
        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        final WishlistDocument document = wishlistMapper.toDocument(wishlist);

        final WishlistDocument wishlistDocumentExpected = JsonMock.getWishlistDocumentExpected();

        assertThat(document).usingRecursiveComparison().isEqualTo(wishlistDocumentExpected);
    }

    @Test
    void shouldMapWishlistToResponse() {
        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        final WishlistResponse response = wishlistMapper.toResponse(wishlist);

        final WishlistResponse wishlistResponseExpected = JsonMock.getWishlistResponseExpected_1();

        assertThat(response).usingRecursiveComparison().isEqualTo(wishlistResponseExpected);
    }

    @Test
    void shouldMapDocumentToDomain() {
        final WishlistDocument document = JsonMock.getWishlistDocument_1();

        final Wishlist wishlist = wishlistMapper.toDomain(document);

        final Wishlist wishlistExpected = JsonMock.getWishlistExpected();

        assertThat(wishlist).usingRecursiveComparison().isEqualTo(wishlistExpected);
    }
}
