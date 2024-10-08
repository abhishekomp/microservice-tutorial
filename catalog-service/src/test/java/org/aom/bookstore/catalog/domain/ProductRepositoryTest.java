package org.aom.bookstore.catalog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.test.database.replace=none",
                "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
        })
@Sql("/test-data.sql")
// @Import(ContainersConfig.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    // You don't need to test the methods provided by Spring Data JPA.
    // This test is to demonstrate how to write tests for the repository layer.
    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(15);
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity product = productRepository.findByCode("P100").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P100");
        assertThat(product.getName()).isEqualTo("The Hunger Games");
        assertThat(product.getDescription()).isEqualTo("Winning will make you famous. Losing means certain death...");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("34.0"));

        ProductEntity expectedProduct = new ProductEntity();
        expectedProduct.setCode("P100");
        expectedProduct.setName("The Hunger Games");

        //assertThat(product).usingRecursiveComparison().ignoringFields("errorDateTime").isEqualTo(expectedApiErr);
        assertThat(product).usingRecursiveComparison().comparingOnlyFields("code", "name").isEqualTo(expectedProduct);
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExists() {
        assertThat(productRepository.findByCode("some invalid code")).isEmpty();
    }
}