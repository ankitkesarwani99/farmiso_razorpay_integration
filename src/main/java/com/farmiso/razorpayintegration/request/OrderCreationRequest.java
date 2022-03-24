package com.farmiso.razorpayintegration.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {

    @NotNull(message = "champion id can't be null")
    @JsonProperty("champion_id")
    private Integer championId;

    @NotNull(message = "amount can't be null")
    @Min(value = 0 ,message = "amount should be greater than or equal to zero")
    @JsonProperty("amount")
    private Float amount;

    @NotBlank(message = "currency can't be null and blank")
    @JsonProperty("currency")
    private String currency;
}
