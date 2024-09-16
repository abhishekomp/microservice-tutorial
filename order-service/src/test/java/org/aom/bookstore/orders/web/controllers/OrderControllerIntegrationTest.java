package org.aom.bookstore.orders.web.controllers;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.aom.bookstore.orders.AbstractIT;
import org.aom.bookstore.orders.domain.model.OrderSummary;
import org.aom.bookstore.orders.testdata.TestDataFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@Sql("/test-orders.sql")
class OrderControllerIntegrationTest extends AbstractIT {

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            System.out.println("Running test: shouldCreateOrderSuccessfully()");
            mockGetProductByCode("P100", "Product 1", new BigDecimal("25.50"));
            var payload =
                    """
                        {
                            "customer" : {
                                "name": "Siva",
                                "email": "siva@gmail.com",
                                "phone": "999999999"
                            },
                            "deliveryAddress" : {
                                "addressLine1": "HNO 123",
                                "addressLine2": "Kukatpally",
                                "city": "Hyderabad",
                                "state": "Telangana",
                                "zipCode": "500072",
                                "country": "India"
                            },
                            "items": [
                                {
                                    "code": "P100",
                                    "name": "Product 1",
                                    "price": 25.50,
                                    "quantity": 1
                                }
                            ]
                        }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldCreateOrderSuccessfullyPathVariable() {
            System.out.println("Running test: shouldCreateOrderSuccessfully()");
            mockGetProductByCodeWithPathVariable("P100", "Product 1", new BigDecimal("28.80"));
            var payload =
                    """
                        {
                            "customer" : {
                                "name": "Siva",
                                "email": "siva@gmail.com",
                                "phone": "999999999"
                            },
                            "deliveryAddress" : {
                                "addressLine1": "HNO 123",
                                "addressLine2": "Kukatpally",
                                "city": "Hyderabad",
                                "state": "Telangana",
                                "zipCode": "500072",
                                "country": "India"
                            },
                            "items": [
                                {
                                    "code": "P100",
                                    "name": "Product 1",
                                    "price": 28.80,
                                    "quantity": 1
                                }
                            ]
                        }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            var payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrderTests {
        @Test
        void getAllOrdersForUser(){
            List<OrderSummary> orders = given()
                    .when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>(){});

            assertThat(orders).hasSize(2);
        }

        @Test
        void getSingleOrdersForUser(){
            String orderNumber = "order-123";
            given()
                    .when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(2));
        }

        @Test
        void shouldReturnNotFoundWhenOrderNotExists() {
            String orderNumber = "invalid_order_number";

            given()
                    .when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(404)
                    .body("status", Matchers.is(404))
                    .body("title", Matchers.is("Order Not Found"))
                    .body("detail", Matchers.is("Order with Number " + orderNumber + " not found"));
        }
    }
}