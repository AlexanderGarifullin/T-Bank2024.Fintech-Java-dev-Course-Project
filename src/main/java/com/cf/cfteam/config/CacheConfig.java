package com.cf.cfteam.config;

import com.cf.cfteam.services.client.CodeforcesClient;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    private final CodeforcesClient codeforcesClient;

    @Bean
    public LoadingCache<String, Double> ratingCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build(codeforcesClient::fetchRatingFromApi);
    }
}
