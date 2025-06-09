package com.netshoes.wishlist.infra.mappers;

import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistInfraMapper {

    WishlistDocument toDocument(final Wishlist wishlist);

    Wishlist toDomain(final WishlistDocument document);

}
