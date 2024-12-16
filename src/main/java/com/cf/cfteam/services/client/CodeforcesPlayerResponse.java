package com.cf.cfteam.services.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeforcesPlayerResponse {

    @JsonProperty("handle")
    private String handle;

    @JsonProperty("rating")
    private Double rating;
}
