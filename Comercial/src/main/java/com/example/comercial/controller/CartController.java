package com.example.comercial.controller;

import com.example.comercial.model.Cart;
import com.example.comercial.model.Payment;
import com.example.comercial.repository.ICartRepository;
import com.example.comercial.repository.IPaymentRepository;
import com.example.comercial.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Iterable<Cart>> findAll(){
        return new ResponseEntity<>(cartService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Iterable<Cart>> findAllByUserId(@PathVariable long id){
        return new ResponseEntity<>(cartService.findAllByUserId(id), HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<Cart> addCart(@RequestBody Cart cart){
        if (cartService.save(cart)){
            return new ResponseEntity<>(cart, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Cart> deleteCart(@PathVariable long id){
        if (cartService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @DeleteMapping("/delete_all/{userId}")
    public ResponseEntity<Cart> deleteAllCart(@PathVariable("userId") long userId){
        if (cartService.deleteAll(userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping("/payment/{id}")
    private ResponseEntity<Payment> payment(@PathVariable("id") long userId){
        if (cartService.payment(userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
