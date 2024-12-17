package com.cf.cfteam.services.client;

import com.cf.cfteam.BaseIntegrationTest;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@WireMockTest(httpPort = 8080)
class CodeforcesClientTest extends BaseIntegrationTest {

    @Autowired
    private CodeforcesClient codeforcesClient;

    private static final String MOCK_JSON_PATH = "src/test/resources/codeforces/responses/";


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("codeforces.api.base.url", () -> "http://localhost:8080/api");
    }

    @Test
    @SneakyThrows
    void fetchRatingFromApi_ShouldReturnRating() {
        String responseBody = Files.readString(Paths.get(MOCK_JSON_PATH + "tourist.json"));

        stubFor(get(urlPathEqualTo("/api/user.info"))
                .withQueryParam("handles", equalTo("tourist"))
                .withQueryParam("checkHistoricHandles", equalTo("false"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

        Double rating = codeforcesClient.fetchRatingFromApi("tourist");

        assertThat(rating).isEqualTo(4009);
    }
}