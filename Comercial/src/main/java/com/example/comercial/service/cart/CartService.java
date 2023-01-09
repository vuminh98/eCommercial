package com.example.comercial.service.cart;

import com.example.comercial.model.cart.Cart;
import com.example.comercial.model.cart.HistoryBuy;
import com.example.comercial.model.cart.Payment;
import com.example.comercial.model.product.Product;

import com.example.comercial.repository.cart.ICartRepository;
import com.example.comercial.repository.cart.IHistoryBuyRepository;
import com.example.comercial.repository.cart.IPaymentRepository;
import com.example.comercial.repository.store.IProductRepository;
import com.example.comercial.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private IHistoryBuyRepository historyBuyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private IProductRepository productRepository;


    public Optional<Cart> findCartById(Long id) {
        return cartRepository.findById(id);
    }
    public boolean save(Cart cart) {
        Optional<Cart> cartUpdate = cartRepository.findByProductAndUser(cart.getProduct(),cart.getUser());
        Product product = productRepository.findById(cart.getProduct().getId()).get();
        double discount = product.getPrice()*(1-(product.getDiscount().doubleValue()/100));
        try {
            if  (cartUpdate.isPresent()) {
                    cartUpdate.get().setQuantity(cartUpdate.get().getQuantity() + cart.getQuantity());
                    double total = discount*cartUpdate.get().getQuantity();
                    cartUpdate.get().setPrice(Math.ceil(total * 100) / 100);
                    cartRepository.save(cartUpdate.get());
            }else{
                double total = discount*cart.getQuantity();
                cart.setPrice(Math.ceil(total * 100) / 100);
                cartRepository.save(cart);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean  delete(Cart cart){
        Optional<Cart> cartDelete = cartRepository.findByProductAndUser(cart.getProduct(),cart.getUser());
        try {
            if  (cartDelete.isPresent()) {
                cartRepository.deleteById(cartDelete.get().getId());
                return true;
            }else{
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Transactional
    public boolean  deleteAll(Long userId) {
        try {
            cartRepository.deleteAllCartByUserId(userId);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Iterable<Cart> findAll(){
        return cartRepository.findAll();
    }
    public Iterable<Cart> findAllByUserId(Long userId){
        return cartRepository.findAllByUserId(userId);
    }
    @Transactional
    public boolean payment(Long userId){
        try {
            double totalPrice = 0;
            List<Cart> carts = (List<Cart>) cartRepository.findAllByUserId(userId);
            paymentRepository.save(new Payment(0L, carts.get(0).getUser(), carts.get(0).getProduct().getStore(),
                    LocalDate.now(),false,0.0));
            Payment payment = paymentRepository.findByUserIdAndStoreId(carts.get(0).getUser().getId(),
                    carts.get(0).getProduct().getStore().getId()).get();
            for (Cart cart : carts) {
                totalPrice += cart.getPrice();
                historyBuyRepository.save(new HistoryBuy(0L,payment,cart.getProduct(),cart.getQuantity()));
            }
            payment.setTotalPrice(totalPrice);
            paymentRepository.save(payment);
//            cartRepository.deleteAllCartByUserId(userId);
            userService.payment(userId,carts.get(0).getProduct().getStore().getUser().getId(),totalPrice);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
