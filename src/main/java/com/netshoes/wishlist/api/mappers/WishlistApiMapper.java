package com.netshoes.wishlist.api.mappers;

import com.netshoes.wishlist.api.jsons.responses.WishlistResponse;
import com.netshoes.wishlist.domain.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistApiMapper {

    @Mapping(target = "totalProducts", expression = "java(wishlist.getProducts().size())")
    WishlistResponse toResponse(final Wishlist wishlist);
}
