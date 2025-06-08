package com.netshoes.wishlist.domain;

import com.netshoes.wishlist.domain.exceptions.ProductAlreadyInWishlistException;
import com.netshoes.wishlist.domain.exceptions.ProductNotInWishlistException;
import com.netshoes.wishlist.domain.exceptions.WishlistLimitReachedException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wishlist {

    private static final int MAX_PRODUCTS = 20;

    private String id;
    private String customerId;
    private List<Product> products;

    public List<Product> getProducts() {
        if (Objects.isNull(products)) {
            return new ArrayList<>();
        }
        return products;
    }

    public static Wishlist of(final String customerId, final Product product) {
        return new Wishlist(null, customerId, List.of(product));
    }

    public boolean isEmpty() {
        return this.getProducts().isEmpty();
    }

    public boolean canAddMoreProducts() {
        return products.size() < MAX_PRODUCTS;
    }

    public boolean containsProduct(final Product product) {
        return products.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));
    }

    public boolean containsProduct(final String productId) {
        return products.stream()
                .anyMatch(p -> productId.equals(p.getId()));
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

    public Wishlist removeProduct(final String productId) {
        final boolean productRemoved = this.getProducts().removeIf(product -> product.getId().equals(productId));

        if (productRemoved) {
            return new Wishlist(
                    this.id,
                    this.customerId,
                    products
            );
        }

        throw new ProductNotInWishlistException(productId);
    }
}
