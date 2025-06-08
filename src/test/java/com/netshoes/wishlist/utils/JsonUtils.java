package com.netshoes.wishlist.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJson(final String filePath, final Class<T> valueType) {
        try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (Objects.isNull(inputStream)) {
                throw new IllegalArgumentException("Arquivo não encontrado: " + filePath);
            }
            return objectMapper.readValue(inputStream, valueType);
        } catch (final IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo JSON: " + filePath, e);
        }
    }
}