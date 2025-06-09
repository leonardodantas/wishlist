package com.netshoes.wishlist.api.jsons.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record ErrorResponse(
        String uuid,
        String message,
        String code,
        String path,
        LocalDateTime localDateTime
) {

    public ErrorResponse(final String message, final String code, final String path) {
        this(UUID.randomUUID().toString(), message, code, null, LocalDateTime.now());
    }
}
