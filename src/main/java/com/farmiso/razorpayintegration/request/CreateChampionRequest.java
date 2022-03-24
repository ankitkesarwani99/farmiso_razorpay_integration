package com.farmiso.razorpayintegration.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateChampionRequest {

    @NotBlank(message = "name can't be blank and null")
    private String name;

    @NotNull(message = "amount can't be null")
    @Min(value = 0,message = "amount can't be negative")
    private Float amount;
}
