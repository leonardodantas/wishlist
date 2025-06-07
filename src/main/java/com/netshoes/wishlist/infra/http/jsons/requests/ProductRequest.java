package com.netshoes.wishlist.infra.http.jsons.requests;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank
        String id
        ) {
}
