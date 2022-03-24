package com.farmiso.razorpayintegration.service;

import com.farmiso.razorpayintegration.constants.Constants;
import com.farmiso.razorpayintegration.entity.Champion;
import com.farmiso.razorpayintegration.entity.Payment;
import com.farmiso.razorpayintegration.entity.PaymentStatus;
import com.farmiso.razorpayintegration.repositery.ChampionRepository;
import com.farmiso.razorpayintegration.repositery.PaymentRepository;
import com.farmiso.razorpayintegration.request.OrderConfirmationRequest;
import com.farmiso.razorpayintegration.request.OrderCreationRequest;
import com.farmiso.razorpayintegration.response.OrderConfirmationResponse;
import com.farmiso.razorpayintegration.response.OrderCreationResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${razorpay.client.secret-key}")
    private String razorpaySecretKey;

    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;
    private final ChampionRepository championRepository;


    /**
     * This method is used to create order_id by making an HTTP call to razorpay server and then saving details in the
     * Payment table.
     *
     * @param orderCreationRequest
     * @return OrderCreationResponse
     * @throws Exception
     */
    @Override
    public OrderCreationResponse createOrder(OrderCreationRequest orderCreationRequest) throws Exception {
        verifyCreateOrderRequest(orderCreationRequest);
        String newOrderId = createNewRazorpayOrder(orderCreationRequest.getAmount(), orderCreationRequest.getCurrency());
        Payment payment = createNewPaymentObject(orderCreationRequest, newOrderId);
        Payment savedPaymentDetails = paymentRepository.save(payment);
        return createOrderCreationResponse(newOrderId, savedPaymentDetails.getId());
    }

    /**
     * This method is used to verify signature received in the request with the signature created by stored order_id
     * in the table .Upon successful verification we stored the received payment_id in the table.
     *
     * @param orderConfirmationRequest
     * @return OrderConfirmationResponse
     * @throws Exception
     */
    @Override
    public OrderConfirmationResponse confirmOrder(OrderConfirmationRequest orderConfirmationRequest) throws Exception {

        Optional<Payment> payment = paymentRepository.findById(orderConfirmationRequest.getPaymentReferenceId());
        if (!payment.isPresent()) {
            log.error("No payment detail found for payment reference id : {} ", orderConfirmationRequest.getPaymentReferenceId());
            throw new BadRequestException("No payment detail found for payment reference id : " + orderConfirmationRequest.getPaymentReferenceId());
        }
        Payment paymentDetail = payment.get();

        if (!PaymentStatus.PENDING.equals(paymentDetail.getPaymentStatus())) {
            throw new BadRequestException("Payment already processed for payment reference id : " + orderConfirmationRequest.getPaymentReferenceId());
        }

        Optional<Champion> champion = championRepository.findById(paymentDetail.getChampionId());
        if (!champion.isPresent()) {
            log.error("No champion exist for payment reference id :{} ", orderConfirmationRequest.getPaymentReferenceId());
            throw new BadRequestException("No champion exist for payment reference id : " + orderConfirmationRequest.getPaymentReferenceId());
        }
        verifySignature(orderConfirmationRequest, paymentDetail);

        boolean paymentStatus = updatePaymentDetailsAndCheckPaymentStatus(champion.get(), orderConfirmationRequest, paymentDetail);

        return OrderConfirmationResponse.builder().success(paymentStatus).build();
    }

    private boolean updatePaymentDetailsAndCheckPaymentStatus(Champion champion, OrderConfirmationRequest orderConfirmationRequest, Payment paymentDetail) throws Exception {
        paymentDetail.setPaymentId(orderConfirmationRequest.getRazorpayPaymentResponse().getRazorpayPaymentId());
        com.razorpay.Payment razorpayPayment;
        try {
            razorpayPayment = razorpayClient.Payments.fetch(orderConfirmationRequest.getRazorpayPaymentResponse().getRazorpayPaymentId());
            log.info("Razorpay payment detail response : {}", razorpayPayment);
        } catch (RazorpayException e) {
            log.error("some error occurred while fetching payment detail from razorpay : {}", e.getMessage());
            throw new Exception(e.getMessage());
        }


        boolean status = true;
        if (razorpayPayment.get("status").toString().equals("captured")) {
            paymentDetail.setPaymentStatus(PaymentStatus.CONFIRMED);
            updateChampionPaymentDetail(champion);
        } else {
            paymentDetail.setPaymentStatus(PaymentStatus.FAILED);
            status = false;
        }
        paymentDetail.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(paymentDetail);
        return status;
    }

    private void updateChampionPaymentDetail(Champion champion) {
        champion.setAmount((float) 0);
        champion.setUpdatedAt(LocalDateTime.now());
        championRepository.save(champion);
    }

    private void verifyCreateOrderRequest(OrderCreationRequest orderCreationRequest) {
        Optional<Champion> champion = championRepository.findById(orderCreationRequest.getChampionId());
        if (!Constants.INDIAN_CURRENCY.equalsIgnoreCase(orderCreationRequest.getCurrency())) {
            throw new BadRequestException("Invalid currency passed in the request");
        }
        if (!champion.isPresent()) {
            throw new BadRequestException("Invalid champion id passed in the request");
        }
        if (!orderCreationRequest.getAmount().equals(champion.get().getAmount())) {
            throw new BadRequestException("Invalid amount passed in the request");
        }
    }

    private Payment createNewPaymentObject(OrderCreationRequest orderCreationRequest, String newOrderId) {

        return Payment.builder()
                .championId(orderCreationRequest.getChampionId())
                .amount(orderCreationRequest.getAmount())
                .paymentStatus(PaymentStatus.PENDING)
                .orderId(newOrderId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    private void verifySignature(OrderConfirmationRequest orderConfirmationRequest, Payment paymentDetail) throws Exception {
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", paymentDetail.getOrderId());
        options.put("razorpay_payment_id", orderConfirmationRequest.getRazorpayPaymentResponse().getRazorpayPaymentId());
        options.put("razorpay_signature", orderConfirmationRequest.getRazorpayPaymentResponse().getRazorpaySignature());

        try {
            if (!Utils.verifyPaymentSignature(options, razorpaySecretKey)) {
                throw new BadRequestException("Invalid payment signature found");
            }
        } catch (RazorpayException e) {
            log.error("some error occurred while validating payment signature for paymentReferenceId : {}, error : {}",
                    orderConfirmationRequest.getPaymentReferenceId(), e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    private String createNewRazorpayOrder(Float amount, String currency) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put(Constants.RazorpayConstants.AMOUNT, Math.round(amount * 100));
        orderRequest.put(Constants.RazorpayConstants.CURRENCY, currency);
        orderRequest.put(Constants.RazorpayConstants.RECEIPT, UUID.randomUUID());

        Order order = null;
        try {
            order = razorpayClient.Orders.create(orderRequest);
        } catch (RazorpayException e) {
            log.error("error while trying to create RazorPay Order : {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
        return order.get("id");
    }

    private OrderCreationResponse createOrderCreationResponse(String orderId, Integer id) {
        return OrderCreationResponse.builder().paymentReferenceId(id).orderId(orderId).build();
    }
}
