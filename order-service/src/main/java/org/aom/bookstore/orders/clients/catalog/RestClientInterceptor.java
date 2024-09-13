package org.aom.bookstore.orders.clients.catalog;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestClientInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //request.getHeaders().add("header1", "header 1 value");
        String rawPath = request.getURI().getRawPath();
        System.out.println("rawPath = " + rawPath);
        String uri = request.getURI().toString();
        System.out.println("uri = " + uri);
        return execution.execute(request, body);
    }

}