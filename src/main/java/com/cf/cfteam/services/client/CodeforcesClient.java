package com.cf.cfteam.services.client;


import com.cf.cfteam.exceptions.client.ClientErrorException;
import com.cf.cfteam.exceptions.client.ServerErrorException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class CodeforcesClient {

    private final RestClient restClient;

    @Value("${codeforces.api.player.url}")
    private String playerUrl;

    public Double fetchRatingFromApi(String login) {
        ResponseEntity<UserInfoResponse> playerResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(playerUrl)
                        .queryParam("handles", login)
                        .queryParam("checkHistoricHandles", false)
                        .build())
                .retrieve()
                .toEntity(UserInfoResponse.class);
        if (playerResponse.getStatusCode().is2xxSuccessful() && playerResponse.getBody() != null &&
        playerResponse.getBody().getResult().size() == 1 && playerResponse.getBody().getResult().getFirst() != null) {
            return playerResponse.getBody().getResult().getFirst().getRating() != null ?
                    playerResponse.getBody().getResult().getFirst().getRating() : 0.;
        }
        if (playerResponse.getStatusCode().is4xxClientError()) {
            throw new ClientErrorException(playerResponse.getStatusCode().toString());
        }
        if (playerResponse.getStatusCode().is5xxServerError()) {
            throw new ServerErrorException(playerResponse.getStatusCode().toString());
        }
        throw new RuntimeException("Error: " + playerResponse.getStatusCode());
    }
}
