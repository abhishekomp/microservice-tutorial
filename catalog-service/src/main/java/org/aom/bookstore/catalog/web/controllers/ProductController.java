package org.aom.bookstore.catalog.web.controllers;

import org.aom.bookstore.catalog.domain.PagedResult;
import org.aom.bookstore.catalog.domain.Product;
import org.aom.bookstore.catalog.domain.ProductNotFoundException;
import org.aom.bookstore.catalog.domain.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
class ProductController {

    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo){
        return productService.getProducts(pageNo);
    }

    @GetMapping("{code}")
    ResponseEntity<Product> getProducts(@PathVariable String code){
        logger.info("ProductController::getProducts() was invoked with code: ", code);
        //String format = String.format("Product with code {} does not exists", code);  //incorrect. should use identifier like %s
        //Product not found with code: %s", code
        return productService.findByCode(code)
                .map(ResponseEntity::ok)
                //.orElseThrow(() -> new ProductNotFoundException(String.format("Product with code %s does not exists", code)));
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }

}
