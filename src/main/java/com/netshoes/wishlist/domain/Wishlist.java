package com.netshoes.wishlist.domain;

import com.netshoes.wishlist.domain.exceptions.ProductAlreadyInWishlistException;
import com.netshoes.wishlist.domain.exceptions.WishlistLimitReachedException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wishlist {

    private static final int MAX_PRODUCTS = 20;

    private String id;
    private String customerId;
    private List<Product> products;

    public static Wishlist of(final String customerId, final Product product) {
        return new Wishlist(null, customerId, List.of(product));
    }

    public boolean canAddMoreProducts() {
        return products.size() < MAX_PRODUCTS;
    }

    public boolean containsProduct(final Product product) {
        return products.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));
    }

    public Wishlist addProduct(final Product product) {

        if (this.containsProduct(product)) {
            throw new ProductAlreadyInWishlistException(product.getId());
        }

        if (Boolean.FALSE.equals(this.canAddMoreProducts())) {
            throw new WishlistLimitReachedException(this.customerId);
        }

        final List<Product> productUpdate = new ArrayList<>(this.products);
        productUpdate.add(product);

        return new Wishlist(
                this.id,
                this.customerId,
                productUpdate
        );
    }
}
