package com.netshoes.wishlist.api.jsons.requests;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank
        String id
        ) {
}
