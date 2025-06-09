package com.netshoes.wishlist.utils;

import com.netshoes.wishlist.domain.Product;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import com.netshoes.wishlist.infra.http.jsons.requests.ProductRequest;
import com.netshoes.wishlist.infra.http.jsons.responses.WishlistResponse;
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

    public static Wishlist getWishlistWith1Products() {
        return JsonUtils.readJson("jsons/domains/wishlist_valid_with_1_products.json", Wishlist.class);
    }

    public static Wishlist getWishlistWith19Products() {
        return JsonUtils.readJson("jsons/domains/wishlist_valid_with_19_products.json", Wishlist.class);
    }

    public static Wishlist getWishlistWith20Products() {
        return JsonUtils.readJson("jsons/domains/wishlist_valid_with_20_products.json", Wishlist.class);
    }

    public static Wishlist getWishlistWithoutProducts() {
        return JsonUtils.readJson("jsons/domains/wishlist_valid_without_products.json", Wishlist.class);
    }

    public static WishlistDocument getWishlistDocumentExpected() {
        return JsonUtils.readJson("jsons/documents/document_expected_1.json", WishlistDocument.class);
    }

    public static WishlistResponse getWishlistResponseExpected_1() {
        return JsonUtils.readJson("jsons/responses/wishlist_expected_1.json", WishlistResponse.class);
    }

    public static WishlistDocument getWishlistDocument() {
        return JsonUtils.readJson("jsons/documents/document_1.json", WishlistDocument.class);
    }

    public static Wishlist getWishlistExpected() {
        return JsonUtils.readJson("jsons/domains/wishlist_domain_expected_1.json", Wishlist.class);
    }

    public static ProductRequest getProductRequest() {
        return JsonUtils.readJson("jsons/requests/product_request.json", ProductRequest.class);
    }

    public static ProductRequest getProductRequestWithIdNull() {
        return new ProductRequest(null);
    }

    public static ProductRequest getProductRequestWithIdEmpty() {
        return new ProductRequest("");
    }

    public static WishlistResponse getWishlistResponseExpected_2() {
        return JsonUtils.readJson("jsons/responses/wishlist_expected_2.json", WishlistResponse.class);
    }

    public static WishlistResponse getWishlistResponseEmpty() {
        return JsonUtils.readJson("jsons/responses/wishlist_expected_empty_1.json", WishlistResponse.class);
    }
}
