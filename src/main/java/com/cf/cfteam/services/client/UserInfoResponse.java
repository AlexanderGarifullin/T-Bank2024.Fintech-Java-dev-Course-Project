package com.cf.cfteam.services.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("result")
    private List<CodeforcesPlayerResponse> result;
}
