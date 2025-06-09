package com.netshoes.wishlist.integration;

import com.netshoes.wishlist.infra.database.documents.WishlistDocument;
import com.netshoes.wishlist.api.jsons.requests.ProductRequest;
import com.netshoes.wishlist.api.jsons.responses.ErrorResponse;
import com.netshoes.wishlist.api.jsons.responses.ProductExistResponse;
import com.netshoes.wishlist.api.jsons.responses.WishlistResponse;
import com.netshoes.wishlist.utils.JsonMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistControllerIntegrationTest extends MongoContainerTest {

    private static final String CUSTOMER_ID = "84c8b0f-4d2e-4a1b-9c3f-5d6e7f8a9b0c";
    private static final String PRODUCT_ID = "e2f3d4c5-b6a7-8901-abcd-ef1234567890";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldSaveWishlistWithOneProduct() {

        final ProductRequest request = JsonMock.getProductRequest();

        final ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/wishlist/{customerId}/products", request, Void.class, CUSTOMER_ID);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNotNull(document);
        assertEquals(CUSTOMER_ID, document.getCustomerId());
        assertEquals(1, document.getProducts().size());

        mongoTemplate.remove(document);
    }

    @Test
    void shouldBadRequestWhereRequestIsNull() {

        final ProductRequest request = JsonMock.getProductRequestWithIdNull();

        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/v1/wishlist/{customerId}/products", request, ErrorResponse.class, CUSTOMER_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNull(document);
    }


    @Test
    void shouldBadRequestWhereRequestIsEmpty() {

        final ProductRequest request = JsonMock.getProductRequestWithIdEmpty();

        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/v1/wishlist/{customerId}/products", request, ErrorResponse.class, CUSTOMER_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNull(document);
    }

    @Test
    void shouldReturnConflictWhereWishlistContains20Products() {

        final WishlistDocument documents = JsonMock.getWishlist20Documents();
        mongoTemplate.save(documents);

        final ProductRequest request = JsonMock.getProductRequest();

        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/v1/wishlist/{customerId}/products", request, ErrorResponse.class, CUSTOMER_ID);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNotNull(document);
        assertFalse(document.getProducts().stream().anyMatch(d -> d.getId().equals(request.id())));

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldReturnConflictWhereProductAlreadyExistInWishlist() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_2();
        mongoTemplate.save(documents);

        final ProductRequest request = JsonMock.getProductRequest();

        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/v1/wishlist/{customerId}/products", request, ErrorResponse.class, CUSTOMER_ID);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNotNull(document);
        assertTrue(document.getProducts().stream().anyMatch(d -> d.getId().equals(request.id())));

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldRemoveProductFromWishlist() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_2();
        mongoTemplate.save(documents);

        final Map<String, String> params = new HashMap<>();
        params.put("customerId", CUSTOMER_ID);
        params.put("productId", PRODUCT_ID);

        final ResponseEntity<Void> response = restTemplate.exchange("/api/v1/wishlist/{customerId}/products/{productId}", HttpMethod.DELETE, null, Void.class, params);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNotNull(document);
        assertFalse(document.getProducts().stream().anyMatch(d -> d.getId().equals(PRODUCT_ID)));

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldNotFoundWhereCustomerIdNotFound() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_1();
        mongoTemplate.save(documents);

        final Map<String, String> params = new HashMap<>();
        params.put("customerId", CUSTOMER_ID);
        params.put("productId", PRODUCT_ID);

        final ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/v1/wishlist/{customerId}/products/{productId}", HttpMethod.DELETE, null, ErrorResponse.class, params);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNull(document);

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldNotFoundWhereProductIdNotFound() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_3();
        mongoTemplate.save(documents);

        final Map<String, String> params = new HashMap<>();
        params.put("customerId", CUSTOMER_ID);
        params.put("productId", PRODUCT_ID);

        final ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/v1/wishlist/{customerId}/products/{productId}", HttpMethod.DELETE, null, ErrorResponse.class, params);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNotNull(document);
        assertFalse(document.getProducts().stream().anyMatch(d -> d.getId().equals(PRODUCT_ID)));

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldRemoveProductFromWishlistAndDeleteWishlist() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_4();
        mongoTemplate.save(documents);

        final Map<String, String> params = new HashMap<>();
        params.put("customerId", CUSTOMER_ID);
        params.put("productId", PRODUCT_ID);

        final ResponseEntity<Void> response = restTemplate.exchange("/api/v1/wishlist/{customerId}/products/{productId}", HttpMethod.DELETE, null, Void.class, params);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        final WishlistDocument document = mongoTemplate.findOne(Query.query(Criteria.where("customerId").is(CUSTOMER_ID)), WishlistDocument.class);
        assertNull(document);
    }

    @Test
    void shouldReturnProductExistsTrueInWishlist() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_2();
        mongoTemplate.save(documents);

        final Map<String, String> params = new HashMap<>();
        params.put("customerId", CUSTOMER_ID);
        params.put("productId", PRODUCT_ID);

        final ResponseEntity<ProductExistResponse> response = restTemplate.getForEntity("/api/v1/wishlist/{customerId}/products/{productId}", ProductExistResponse.class, params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().exists());

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldReturnProductExistsFalseInWishlist() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_3();
        mongoTemplate.save(documents);

        final Map<String, String> params = new HashMap<>();
        params.put("customerId", CUSTOMER_ID);
        params.put("productId", PRODUCT_ID);

        final ResponseEntity<ProductExistResponse> response = restTemplate.getForEntity("/api/v1/wishlist/{customerId}/products/{productId}", ProductExistResponse.class, params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().exists());

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldReturnNotFoundWhereVerifyProductExistWithCustomerWithoutWishlist() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_1();
        mongoTemplate.save(documents);

        final Map<String, String> params = new HashMap<>();
        params.put("customerId", CUSTOMER_ID);
        params.put("productId", PRODUCT_ID);

        final ResponseEntity<Void> response = restTemplate.getForEntity("/api/v1/wishlist/{customerId}/products/{productId}", Void.class, params);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldReturnWishlistResponseWithEmptyProducts() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_1();
        mongoTemplate.save(documents);

        final ResponseEntity<WishlistResponse> response = restTemplate.getForEntity("/api/v1/wishlist/{customerId}/products", WishlistResponse.class, CUSTOMER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CUSTOMER_ID, response.getBody().customerId());
        assertTrue(response.getBody().products().isEmpty());

        mongoTemplate.remove(documents);
    }

    @Test
    void shouldReturnWishlistResponseWithProducts() {

        final WishlistDocument documents = JsonMock.getWishlistDocument_2();
        mongoTemplate.save(documents);

        final ResponseEntity<WishlistResponse> response = restTemplate.getForEntity("/api/v1/wishlist/{customerId}/products", WishlistResponse.class, CUSTOMER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CUSTOMER_ID, response.getBody().customerId());
        assertFalse(response.getBody().products().isEmpty());
        assertEquals(11, response.getBody().products().size());

        mongoTemplate.remove(documents);
    }

}
