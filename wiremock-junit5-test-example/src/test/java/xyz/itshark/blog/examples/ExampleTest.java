package xyz.itshark.blog.examples;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8089)
public class ExampleTest {

    private HttpResponse<String> makeCallTo(String path) {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                        URI.create("http://localhost:8089"+ path))
                .header("accept", "application/json")
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            return null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Test
    public void exampleTest() {
        stubFor(get("/some/data")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"data\":\"some data\"}")));

        HttpResponse<String> response = makeCallTo("/some/data");

        assertTrue ("{\"data\":\"some data\"}".equals(response.body()));

        verify(getRequestedFor(urlPathEqualTo("/some/data"))
                .withHeader("accept", equalTo("application/json"))
        );
    }

}

