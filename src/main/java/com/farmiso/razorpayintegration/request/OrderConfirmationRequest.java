package com.farmiso.razorpayintegration.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderConfirmationRequest {

    @NotNull(message = "payment_reference_id can't be null")
    @JsonProperty("payment_reference_id")
    private Integer paymentReferenceId;

    @NotNull(message = "razorpay_payment_response can't be null")
    @JsonProperty("razorpay_payment_response")
    private RazorpayPaymentResponse razorpayPaymentResponse;

    @Data
    public static class RazorpayPaymentResponse {
        @NotNull(message = "razorpay_payment_id can't be null")
        @JsonProperty("razorpay_payment_id")
        private String razorpayPaymentId;

        @NotNull(message = "razorpay_order_id can't be null")
        @JsonProperty("razorpay_order_id")
        private String razorpayOrderId;

        @NotNull(message = "razorpay_signature can't be null")
        @JsonProperty("razorpay_signature")
        private String razorpaySignature;
    }
}
