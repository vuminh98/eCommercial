package com.example.comercial.controller;


import com.example.comercial.model.cart.Payment;
import com.example.comercial.service.cart.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{userId}")
    public ResponseEntity<Iterable<Payment>> findAllByUser(@PathVariable Long userId){
        return new ResponseEntity<>(paymentService.findAllByUserId(userId), HttpStatus.OK);
    }
}
