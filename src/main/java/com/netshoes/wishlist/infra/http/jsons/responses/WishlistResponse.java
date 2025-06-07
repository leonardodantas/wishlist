package com.netshoes.wishlist.infra.http.jsons.responses;

import java.util.List;

public record WishlistResponse(
        String id,
        String customerId,
        List<ProductResponse> products,
        int totalProducts
) {
}
