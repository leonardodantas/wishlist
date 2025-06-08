package com.netshoes.wishlist.infra.database.documents;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "products")
public class ProductDocument {

    private String id;

}
