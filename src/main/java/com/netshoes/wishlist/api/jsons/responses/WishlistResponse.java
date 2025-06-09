package com.netshoes.wishlist.api.jsons.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WishlistResponse(
        String id,
        String customerId,
        List<ProductResponse> products,
        int totalProducts
) {
    public static WishlistResponse of(final String customerId) {
        return new WishlistResponse(null, customerId, List.of(), 0);
    }
}
