package com.netshoes.wishlist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netshoes.wishlist.api.controllers.WishlistController;
import com.netshoes.wishlist.api.jsons.requests.ProductRequest;
import com.netshoes.wishlist.api.jsons.responses.ProductExistResponse;
import com.netshoes.wishlist.api.jsons.responses.WishlistResponse;
import com.netshoes.wishlist.api.mappers.WishlistApiMapper;
import com.netshoes.wishlist.api.mappers.WishlistApiMapperImpl;
import com.netshoes.wishlist.app.usecases.AddProductToWishlistUseCase;
import com.netshoes.wishlist.app.usecases.FindWishlistByCustomerIdUseCase;
import com.netshoes.wishlist.app.usecases.ProductExistsInWishlistUseCase;
import com.netshoes.wishlist.app.usecases.RemoveProductFromWishlistUseCase;
import com.netshoes.wishlist.domain.Product;
import com.netshoes.wishlist.domain.Wishlist;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WishlistController.class)
@Import(WishlistApiMapperImpl.class)
public class WishlistControllerTest {

    private static final String CUSTOMER_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";
    private static final String PRODUCT_ID = "12345678-1234-1234-1234-123456789012";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddProductToWishlistUseCase addProductToWishlistUseCase;

    @MockitoBean
    private RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @MockitoBean
    private ProductExistsInWishlistUseCase productExistsInWishlistUseCase;

    @MockitoBean
    private FindWishlistByCustomerIdUseCase findWishlistByCustomerIdUseCase;

    @Test
    void shouldAddProductToWishlistAndReturn201() throws Exception {

        final ProductRequest request = JsonMock.getProductRequest();

        mockMvc.perform(post("/api/v1/wishlist/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(addProductToWishlistUseCase).execute(anyString(), any(Product.class));
    }

    @Test
    void shouldReturn400WhenProductRequestIsNull() throws Exception {

        final ProductRequest request = JsonMock.getProductRequestWithIdNull();

        mockMvc.perform(post("/api/v1/wishlist/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(addProductToWishlistUseCase, never()).execute(anyString(), any(Product.class));
    }

    @Test
    void shouldReturn400WhenProductRequestIsEmpty() throws Exception {

        final ProductRequest request = JsonMock.getProductRequestWithIdEmpty();

        mockMvc.perform(post("/api/v1/wishlist/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(addProductToWishlistUseCase, never()).execute(anyString(), any(Product.class));
    }

    @Test
    void shouldRemoveProductFromWishlistAndReturn204() throws Exception {

        mockMvc.perform(delete("/api/v1/wishlist/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(removeProductFromWishlistUseCase).execute(anyString(), anyString());
    }

    @Test
    void shouldProductExistTrue() throws Exception {

        when(productExistsInWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID)).thenReturn(Boolean.TRUE);

        final ResultActions resultActions = mockMvc.perform(get("/api/v1/wishlist/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(JsonMock.getProductRequest())))
                .andExpect(status().isOk());

        verify(productExistsInWishlistUseCase).execute(anyString(), anyString());

        final String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        final ProductExistResponse response = objectMapper.readValue(responseAsString, ProductExistResponse.class);
        assertTrue(response.exists());
    }

    @Test
    void shouldProductExistFalse() throws Exception {

        when(productExistsInWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID)).thenReturn(Boolean.FALSE);

        final ResultActions resultActions = mockMvc.perform(get("/api/v1/wishlist/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(JsonMock.getProductRequest())))
                .andExpect(status().isOk());

        verify(productExistsInWishlistUseCase).execute(anyString(), anyString());

        final String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        final ProductExistResponse response = objectMapper.readValue(responseAsString, ProductExistResponse.class);
        assertFalse(response.exists());
    }

    @Test
    void shouldGetWishlistByCustomerIdAndReturn200() throws Exception {

        final Wishlist wishlist = JsonMock.getWishlistWith20Products();
        when(findWishlistByCustomerIdUseCase.execute(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        final ResultActions resultActions = mockMvc.perform(get("/api/v1/wishlist/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(findWishlistByCustomerIdUseCase).execute(anyString());

        final String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        final WishlistResponse response = objectMapper.readValue(responseAsString, WishlistResponse.class);

        final WishlistResponse expectedResponse = JsonMock.getWishlistResponseExpected_2();
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void shouldGetWishlistEmptyByCustomerIdAndReturn200() throws Exception {

        when(findWishlistByCustomerIdUseCase.execute(CUSTOMER_ID)).thenReturn(Optional.empty());

        final ResultActions resultActions = mockMvc.perform(get("/api/v1/wishlist/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(findWishlistByCustomerIdUseCase).execute(anyString());

        final String responseAsString = resultActions.andReturn().getResponse().getContentAsString();
        final WishlistResponse response = objectMapper.readValue(responseAsString, WishlistResponse.class);

        final WishlistResponse expectedResponse = JsonMock.getWishlistResponseEmpty();
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

}
