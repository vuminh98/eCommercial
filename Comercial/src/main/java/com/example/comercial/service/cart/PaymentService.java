package com.example.comercial.service.cart;

import com.example.comercial.model.Cart;
import com.example.comercial.model.Payment;
import com.example.comercial.repository.ICartRepository;
import com.example.comercial.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class PaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;


    private void save(Payment payment){
        paymentRepository.save(payment);
    }
}
