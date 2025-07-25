package com.netshoes.wishlist.api.controllers;

import com.netshoes.wishlist.api.jsons.requests.ProductRequest;
import com.netshoes.wishlist.api.jsons.responses.ErrorResponse;
import com.netshoes.wishlist.api.jsons.responses.ProductExistResponse;
import com.netshoes.wishlist.api.jsons.responses.WishlistResponse;
import com.netshoes.wishlist.api.mappers.WishlistApiMapper;
import com.netshoes.wishlist.app.usecases.AddProductToWishlistUseCase;
import com.netshoes.wishlist.app.usecases.FindWishlistByCustomerIdUseCase;
import com.netshoes.wishlist.app.usecases.ProductExistsInWishlistUseCase;
import com.netshoes.wishlist.app.usecases.RemoveProductFromWishlistUseCase;
import com.netshoes.wishlist.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wishlist")
@Tag(name = "Wishlist", description = "Endpoints para gerenciar a wishlist dos clientes")
public class WishlistController {

    private final WishlistApiMapper wishlistApiMapper;
    private final AddProductToWishlistUseCase addProductToWishlistUseCase;
    private final RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;
    private final ProductExistsInWishlistUseCase productExistsInWishlistUseCase;
    private final FindWishlistByCustomerIdUseCase findWishlistByCustomerIdUseCase;

    @Operation(
            summary = "Adicionar produto à wishlist",
            description = "Adiciona um produto à wishlist do cliente especificado pelo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto adicionado com sucesso à wishlist do cliente"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Produto já existe na wishlist do cliente ou limite de produtos atingido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("{customerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProductToWishlist(@PathVariable final String customerId, @Valid @RequestBody final ProductRequest request) {
        log.info("Adicionando produto à wishlist do cliente: {}", customerId);
        final Product product = Product.builder()
                .id(request.id())
                .build();

        addProductToWishlistUseCase.execute(customerId, product);
    }

    @Operation(
            summary = "Remover produto da wishlist",
            description = "Remove um produto da wishlist do cliente especificado pelo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso da wishlist do cliente"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado na wishlist do cliente ou cliente não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{customerId}/products/{productId}")
    public void removeProductFromWishlist(@PathVariable final String customerId, @PathVariable final String productId) {
        log.info("Removendo produto da wishlist do cliente: {}", customerId);
        removeProductFromWishlistUseCase.execute(customerId, productId);
    }

    @Operation(
            summary = "Verificar se produto existe na wishlist",
            description = "Verifica se um produto existe na wishlist do cliente especificado pelo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado ou não na wishlist do cliente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductExistResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Wishlist não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{customerId}/products/{productId}")
    public ProductExistResponse productExistsInWishlist(@PathVariable final String customerId, @PathVariable final String productId) {
        log.info("Verificando se o produto {} existe na wishlist do cliente: {}", productId, customerId);
        final Boolean productExist = productExistsInWishlistUseCase.execute(customerId, productId);
        log.info("Produto {} existe na wishlist do cliente {}? {}", productId, customerId, productExist);
        return new ProductExistResponse(productExist);
    }

    @Operation(
            summary = "Obter wishlist do cliente",
            description = "Obtém a wishlist do cliente especificado pelo ID. Se a lista não existir, retorna uma lista vazia."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado ou não na wishlist do cliente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("{customerId}/products")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable final String customerId) {
        log.info("Obtendo wishlist do cliente: {}", customerId);
        return findWishlistByCustomerIdUseCase.execute(customerId)
                .map(wishlist -> {
                    final WishlistResponse response = wishlistApiMapper.toResponse(wishlist);
                    log.info("Wishlist do cliente {} obtida com sucesso", customerId);
                    return ResponseEntity.ok(response);
                }).orElseGet(() -> {
                    final WishlistResponse response = WishlistResponse.of(customerId);
                    log.info("Wishlist do cliente {} não encontrada, retornando lista vazia", customerId);
                    return ResponseEntity.ok(response);
                });
    }
}
