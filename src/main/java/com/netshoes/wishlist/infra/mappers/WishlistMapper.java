package com.netshoes.wishlist.infra.mappers;

import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import com.netshoes.wishlist.infra.http.jsons.responses.WishlistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    WishlistDocument toDocument(final Wishlist wishlist);

    Wishlist toDomain(final WishlistDocument document);

    @Mapping(target = "totalProducts", expression = "java(wishlist.getProducts().size())")
    WishlistResponse toResponse(final Wishlist wishlist);
}
