package com.netshoes.wishlist.infra.database.repositories;

import com.netshoes.wishlist.app.repositories.IWishlistRepository;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import com.netshoes.wishlist.infra.mappers.WishlistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WishListRepository implements IWishlistRepository {

    private final WishlistMapper wishlistMapper;
    private final WishListMongoRepository wishListMongoRepository;

    @Override
    public Optional<Wishlist> findByCustomerId(final String customerId) {
        return wishListMongoRepository.findByCustomerId(customerId)
                .map(wishlistMapper::toDomain);
    }

    @Override
    public void save(final Wishlist wishlist) {
        final WishlistDocument document = wishlistMapper.toDocument(wishlist);
        wishListMongoRepository.save(document);
    }

    @Override
    public void deleteById(final String id) {
        wishListMongoRepository.deleteById(id);
    }
}
