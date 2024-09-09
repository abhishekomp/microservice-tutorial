package org.aom.bookstore.catalog.domain;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String msg) {
        super(msg);
    }
    public static ProductNotFoundException forCode(String code){
        //String format = String.format("Product with code {} does not exists", code); //incorrect. should use %s or relevant identifier.
        //String format = String.format("Product with code %s does not exists", code);
        return new ProductNotFoundException(String.format("Product not found with code: %s", code));
    }
}
