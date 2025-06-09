package com.netshoes.wishlist.mappers;

import com.netshoes.wishlist.api.jsons.responses.WishlistResponse;
import com.netshoes.wishlist.api.mappers.WishlistApiMapper;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class WishlistApiMapperTest {

    private final WishlistApiMapper wishlistApiMapper = Mappers.getMapper(WishlistApiMapper.class);

    @Test
    void shouldMapWishlistToResponse() {
        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        final WishlistResponse response = wishlistApiMapper.toResponse(wishlist);

        final WishlistResponse wishlistResponseExpected = JsonMock.getWishlistResponseExpected_1();

        assertThat(response).usingRecursiveComparison().isEqualTo(wishlistResponseExpected);
    }

}
