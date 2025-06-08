package com.netshoes.wishlist.utils;

import com.netshoes.wishlist.domain.Product;
import com.netshoes.wishlist.domain.Wishlist;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonMock {

    public static Product getProductValid_1() {
        return JsonUtils.readJson("jsons/domains/product_valid_1.json", Product.class);
    }

    public static Wishlist getWishlistValid_1() {
        return JsonUtils.readJson("jsons/domains/wishlist_valid_1.json", Wishlist.class);
    }

    public static Wishlist getWishlistValid_2() {
        return JsonUtils.readJson("jsons/domains/wishlist_valid_2.json", Wishlist.class);
    }

    public static Wishlist getWishlistWith20Products() {
        return JsonUtils.readJson("jsons/domains/wishlist_valid_with_20_products.json", Wishlist.class);
    }
}
