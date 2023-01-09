package com.example.comercial.controller;

import com.example.comercial.model.HistoryBuy;
import com.example.comercial.model.Payment;
import com.example.comercial.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin("*")
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @GetMapping("/{storeId}")
    public ResponseEntity<Iterable<Payment>> findAllPaymentByStore(@PathVariable Long storeId) {
        try {
            if (storeService.paymentByStore(storeId) != null) {
                return new ResponseEntity<>(storeService.paymentByStore(storeId), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/search_payments")
    public ResponseEntity<Iterable<Payment>> findAllPaymentBySearch(@RequestPart("key") String key,
                                                                    @RequestPart("value") String value,
                                                                    @RequestPart("storeId") String storeId) {
        try {
            return new ResponseEntity<>(storeService.paymentsBySearch(key,value,storeId),HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/one_user")
    public ResponseEntity<Iterable<Payment>> findAllPaymentByUser(@RequestParam("user") Long user,
                                                                  @RequestParam("store") Long store) {
        try {
            if (storeService.paymentByOneUser(user,store) != null) {
                return new ResponseEntity<>(storeService.paymentByOneUser(user,store), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/detail_payment/{paymentId}")
    public ResponseEntity<Iterable<HistoryBuy>> paymentDetail(@PathVariable Long paymentId) {
        try {
            if (storeService.paymentDetails(paymentId) != null) {
                return new ResponseEntity<>(storeService.paymentDetails(paymentId), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
