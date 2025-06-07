package com.netshoes.wishlist.infra.http.jsons.responses;

import java.time.LocalDateTime;

public record ErrorResponse(
        String uuid,
        String message,
        String code,
        LocalDateTime localDateTime
) {
}
