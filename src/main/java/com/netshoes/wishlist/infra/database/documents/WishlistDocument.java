package com.netshoes.wishlist.infra.database.documents;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@Document(collection = "wishlists")
public class WishlistDocument {

    private String id;
    private String userId;
    private List<ProductDocument> products;
    private BigDecimal totalPrice;

}
