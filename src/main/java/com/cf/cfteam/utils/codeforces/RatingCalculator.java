package com.cf.cfteam.utils.codeforces;

import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;

import java.util.List;

public final class RatingCalculator {

    public static double getWinProbability(double ra, double rb) {
        return 1.0 / (1.0 + Math.pow(10.0, (rb - ra) / 400.0));
    }

    public static double aggregateRatings(List<PlayerResponse> players) {
        double left = 1.0;
        double right = 1E4;

        for (int tt = 0; tt < 100; tt++) {
            double r = (left + right) / 2.0;

            double rWinsProbability = 1.0;
            for (PlayerResponse player : players) {
                rWinsProbability *= getWinProbability(r, player.rating());
            }

            double rating = Math.log10(1 / rWinsProbability - 1) * 400 + r;

            if (rating > r) {
                left = r;
            } else {
                right = r;
            }
        }

        return (left + right) / 2.0;
    }

    private RatingCalculator() {
        throw new UnsupportedOperationException("Utility class");
    }
}
