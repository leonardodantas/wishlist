package com.netshoes.wishlist.infra.database.documents;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Builder
@Document(collection = "products")
public class ProductDocument {

    private String id;
    private BigDecimal price;

}
