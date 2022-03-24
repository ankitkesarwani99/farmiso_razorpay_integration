package com.farmiso.razorpayintegration.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCreationResponse {
    @JsonProperty("payment_reference_id")
    private Integer paymentReferenceId;
    @JsonProperty("order_id")
    private String orderId;

}
