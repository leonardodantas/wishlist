package com.netshoes.wishlist.mappers;

import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import com.netshoes.wishlist.infra.mappers.WishlistInfraMapper;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class WishlistInfraMapperTest {

    private final WishlistInfraMapper wishlistInfraMapper = Mappers.getMapper(WishlistInfraMapper.class);

    @Test
    void shouldMapWishlistToDocument() {
        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        final WishlistDocument document = wishlistInfraMapper.toDocument(wishlist);

        final WishlistDocument wishlistDocumentExpected = JsonMock.getWishlistDocumentExpected();

        assertThat(document).usingRecursiveComparison().isEqualTo(wishlistDocumentExpected);
    }

    @Test
    void shouldMapDocumentToDomain() {
        final WishlistDocument document = JsonMock.getWishlistDocument_1();

        final Wishlist wishlist = wishlistInfraMapper.toDomain(document);

        final Wishlist wishlistExpected = JsonMock.getWishlistExpected();

        assertThat(wishlist).usingRecursiveComparison().isEqualTo(wishlistExpected);
    }
}
