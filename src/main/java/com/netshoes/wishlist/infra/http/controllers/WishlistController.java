package com.netshoes.wishlist.infra.http.controllers;

import com.netshoes.wishlist.domain.Product;
import com.netshoes.wishlist.app.usecases.AddProductToWishlistUseCase;
import com.netshoes.wishlist.infra.http.jsons.requests.ProductRequest;
import com.netshoes.wishlist.infra.http.jsons.responses.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wishlist")
@Tag(name = "Wishlist", description = "Endpoints para gerenciar a lista de desejos dos clientes")
public class WishlistController {

    private final AddProductToWishlistUseCase addProductToWishlistUseCase;


    @Operation(
            summary = "Adicionar produto à lista de desejos",
            description = "Adiciona um produto à lista de desejos do cliente especificado pelo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto adicionado com sucesso à lista de desejos do cliente"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Produto já existe na lista de desejos do cliente ou limite de produtos atingido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("{customerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProductToWishlist(@PathVariable final String customerId, @Valid @RequestBody final ProductRequest request) {
        final Product product = Product.builder()
                .id(request.id())
                .build();

        addProductToWishlistUseCase.execute(customerId, product);
    }
}
