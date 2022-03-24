package com.farmiso.razorpayintegration.controller;

import com.farmiso.razorpayintegration.request.OrderConfirmationRequest;
import com.farmiso.razorpayintegration.request.OrderCreationRequest;
import com.farmiso.razorpayintegration.response.GenericResponse;
import com.farmiso.razorpayintegration.response.OrderConfirmationResponse;
import com.farmiso.razorpayintegration.response.OrderCreationResponse;
import com.farmiso.razorpayintegration.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;


@RestController
@EnableWebMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@CrossOrigin
@RequestMapping("/api/v1/champion/order")
public class OrderController {

    private final OrderService orderService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> createOrder(@Valid @RequestBody OrderCreationRequest orderCreationRequest) throws Exception {

        log.info("OrderCreationRequest : {}", orderCreationRequest);

        GenericResponse<OrderCreationResponse> genericResponse = new GenericResponse();
        OrderCreationResponse orderCreationResponse = orderService.createOrder(orderCreationRequest);
        genericResponse.setData(orderCreationResponse);
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(genericResponse, httpStatus);
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> confirmOrder(@Valid @RequestBody OrderConfirmationRequest orderConfirmationRequest) throws Exception {
        log.info("OrderConfirmationRequest : {}", orderConfirmationRequest);
        GenericResponse<OrderConfirmationResponse> genericResponse = new GenericResponse();
        OrderConfirmationResponse orderConfirmationResponse = orderService.confirmOrder(orderConfirmationRequest);
        genericResponse.setData(orderConfirmationResponse);
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(genericResponse, httpStatus);
    }
}
