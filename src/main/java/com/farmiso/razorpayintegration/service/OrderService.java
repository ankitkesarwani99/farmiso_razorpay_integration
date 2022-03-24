package com.farmiso.razorpayintegration.service;

import com.farmiso.razorpayintegration.request.OrderConfirmationRequest;
import com.farmiso.razorpayintegration.request.OrderCreationRequest;
import com.farmiso.razorpayintegration.response.OrderConfirmationResponse;
import com.farmiso.razorpayintegration.response.OrderCreationResponse;

public interface OrderService {
    OrderCreationResponse createOrder(OrderCreationRequest orderCreationRequest) throws Exception;
    OrderConfirmationResponse confirmOrder(OrderConfirmationRequest orderConfirmationRequest) throws Exception;
}
