package org.aom.bookstore.webapp.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/cart")
    String cart() {
        return "cart";
    }

}