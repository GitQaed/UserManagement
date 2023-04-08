package main.utils;

import org.springframework.http.HttpHeaders;

public class CustomHttpHeaders {

    public static HttpHeaders addHeaderJwt(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + token);
        return responseHeaders;
    }

}
