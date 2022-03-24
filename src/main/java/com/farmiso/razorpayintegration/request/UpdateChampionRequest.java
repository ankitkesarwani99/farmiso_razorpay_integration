package com.farmiso.razorpayintegration.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateChampionRequest {

    @NotNull(message = "champion id can't be null")
    @JsonProperty("champion_id")
    private Integer championId;

    private String name;

    @Min(value = 0,message = "amount can't be negative")
    private Float amount;
}
