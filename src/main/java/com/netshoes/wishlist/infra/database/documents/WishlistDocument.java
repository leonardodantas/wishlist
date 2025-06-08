package com.netshoes.wishlist.infra.database.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "wishlists")
public class WishlistDocument {

    @Id
    private String id;
    @Indexed(unique = true)
    private String customerId;
    private List<ProductDocument> products;

}
