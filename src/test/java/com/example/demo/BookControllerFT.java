package com.example.demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import java.util.function.Function;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BookControllerFT {

//    // TODO - need to be changed to random port
    private final Function<String,String> urlWithoutPort = port -> "http://localhost:"+port;

    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Test
    public void verifyCreate() {

        String requestBody = """
                {
                    "title": "Kroniki Jakuba Wędrowycza",
                    "author": "Andrzej Pilipiuk"
                }
                """;

        String port = String.valueOf(webServerAppCtxt.getWebServer().getPort());
        String url = urlWithoutPort.apply(port);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .log().all()
                .baseUri(url)
                .when()
                .post("/books", requestBody);

        log.info(response.getBody().prettyPrint());

        response
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void verifyRead() {

        String port = String.valueOf(webServerAppCtxt.getWebServer().getPort());
        String url = urlWithoutPort.apply(port);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .log().all()
                .baseUri(url)
                .when()
                .get("/books");

        log.info(response.getBody().prettyPrint());

        response
                .then()
                .log().all()
                .statusCode(200);
    }

}
