package org.aom.bookstore.orders;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public abstract class AbstractIT {

    @LocalServerPort
    int port;
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.8.0-alpine");
    @BeforeAll
    static void beforeAll() {

        wiremockServer.start();
        configureFor(wiremockServer.getHost(), wiremockServer.getPort());
        //System.out.println(wiremockServer.getCurrentContainerInfo().getHostnamePath());
        //String host = wiremockServer.getHost();
        //System.out.println("host = " + host);
        //Integer port1 = wiremockServer.getPort();
        //Integer port1 = wiremockServer.getExposedPorts().get(0);
        //System.out.println("Mapped port: " + wiremockServer.getExposedPorts());

    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
//        String baseUrl = wiremockServer.getBaseUrl().toString();
//        System.out.println("baseUrl = " + baseUrl);
        registry.add("orders.catalog-service-url", wiremockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
    }

    protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
        System.out.println("Wiremock stub was invoked with code: " + code);
        //stubFor(get(url("/api/products/P100"))
        stubFor(get(urlMatching("/api/products/P100"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(
                                """
                    {
                        "code": "%s",
                        "name": "%s",
                        "price": %s
                    }
                """
                                        .formatted(code, name, (price.doubleValue())))));

//        stubFor(get(urlPathTemplate("/api/products/{code}"))
//                .withPathParam("code", equalTo("P100"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//                        .withStatus(200)
//                        .withBody(
//                                """
//                    {
//                        "code": "%s",
//                        "name": "%s",
//                        "price": %f
//                    }
//                """
//                                        .formatted(code, name, price.doubleValue()))));
    }

}
