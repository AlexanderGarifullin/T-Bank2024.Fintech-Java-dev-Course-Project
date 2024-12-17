package com.cf.cfteam.utils.codeforces;

import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class RatingCalculatorTest {

    @Test
    void testCalculateSoloRating() {
        PlayerResponse response = PlayerResponse.builder()
                .id(1L)
                .login("login")
                .rating(1000.)
                .build();
        List<PlayerResponse> players = List.of(response);
        var result = RatingCalculator.aggregateRatings(players);

        assertThat(result).isEqualTo(1000.);
    }
}