package org.aom.bookstore.orders.testdata;

import org.aom.bookstore.orders.domain.model.Address;
import org.aom.bookstore.orders.domain.model.CreateOrderRequest;
import org.aom.bookstore.orders.domain.model.Customer;
import org.aom.bookstore.orders.domain.model.OrderItem;
import org.instancio.Instancio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.instancio.Select.field;

public class TestDataFactory {

    static final List<String> VALID_COUNTRIES = List.of("India", "Germany");
    static final Set<OrderItem> VALID_ORDER_ITEMS =
            Set.of(new OrderItem("P100", "Product 1", new BigDecimal("25.50"), 1));
    static final Set<OrderItem> INVALID_ORDER_ITEMS =
            Set.of(new OrderItem("ABCD", "Product 1", new BigDecimal("25.50"), 1));

    public static CreateOrderRequest createValidOrderRequest() {
        CreateOrderRequest orderRequest = Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();
        System.out.println("orderRequest = " + orderRequest);
        return orderRequest;
    }

    public static CreateOrderRequest createOrderRequestWithInvalidCustomer() {
        CreateOrderRequest orderRequest = Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                .set(field(Customer::phone), "")
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .create();
        return orderRequest;
    }

    public static CreateOrderRequest createOrderRequestWithInvalidDeliveryAddress() {
        CreateOrderRequest orderRequest = Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#d#d_#c#c@mail.com"))
                .set(field(Address::country), "")
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .create();
        return orderRequest;
    }

    public static CreateOrderRequest createOrderRequestWithNoItems() {
        CreateOrderRequest orderRequest = Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#c#c#d#d@mail.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .set(field(CreateOrderRequest::items), Set.of())
                .create();
        return orderRequest;
    }

    public static void main(String[] args) {
        //System.out.println(TestDataFactory.createValidOrderRequest());
        //System.out.println(TestDataFactory.createOrderRequestWithInvalidCustomer());
        //System.out.println(TestDataFactory.createOrderRequestWithInvalidDeliveryAddress());
        System.out.println(TestDataFactory.createOrderRequestWithNoItems());
    }

}
