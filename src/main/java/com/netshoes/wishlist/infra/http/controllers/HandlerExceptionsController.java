package com.netshoes.wishlist.infra.http.controllers;

import com.netshoes.wishlist.domain.exceptions.ProductAlreadyInWishlistException;
import com.netshoes.wishlist.domain.exceptions.ProductNotInWishlistException;
import com.netshoes.wishlist.domain.exceptions.WishlistLimitReachedException;
import com.netshoes.wishlist.domain.exceptions.WishlistNotFoundException;
import com.netshoes.wishlist.infra.http.jsons.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class HandlerExceptionsController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex, final WebRequest request) {
        final ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                request.getContextPath());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex, final WebRequest request) {

        final ErrorResponse response = new ErrorResponse(
                ex.getBody().toString(),
                "VALIDATION_ERROR",
                request.getContextPath());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ProductAlreadyInWishlistException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyInWishlist(final ProductAlreadyInWishlistException ex, final WebRequest request) {
        final ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                "PRODUCT_ALREADY_IN_WISHLIST",
                request.getContextPath());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(WishlistLimitReachedException.class)
    public ResponseEntity<ErrorResponse> handleWishlistLimitReached(final WishlistLimitReachedException ex, final WebRequest request) {
        final ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                "WISHLIST_LIMIT_REACHED",
                request.getContextPath());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ProductNotInWishlistException.class)
    public ResponseEntity<ErrorResponse> handleProductNotInWishlist(final ProductNotInWishlistException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(WishlistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWishlistNotFound(final WishlistNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
