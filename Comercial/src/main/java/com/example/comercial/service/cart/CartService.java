package com.example.comercial.service;

import com.example.comercial.model.Cart;
import com.example.comercial.model.HistoryBuy;
import com.example.comercial.model.Payment;
import com.example.comercial.model.User;
import com.example.comercial.repository.ICartRepository;
import com.example.comercial.repository.IHistoryBuyRepository;
import com.example.comercial.repository.IPaymentRepository;
import com.example.comercial.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
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
    private IUserRepository userRepository;


    public Optional<Cart> findCartById(Long id) {
        return cartRepository.findById(id);
    }
    public boolean save(Cart cart) {
        Optional<Cart> cartUpdate = cartRepository.findByProductAndUser(cart.getProduct(),cart.getUser());
        try {
            if  (cartUpdate.isPresent()) {
                cartUpdate.get().setQuantity(cartUpdate.get().getQuantity()+cart.getQuantity());
                cartUpdate.get().setPrice(cartUpdate.get().getPrice()*cartUpdate.get().getQuantity());
                cartRepository.save(cartUpdate.get());
            }else{
                cart.setPrice(cart.getProduct().getPrice()*cart.getQuantity());
                cartRepository.save(cart);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean  delete(Long id){
        Optional<Cart> cart = cartRepository.findById(id);
        try {
            if  (cart.isPresent()) {
                cartRepository.deleteById(id);
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
            paymentRepository.save(new Payment(0L, carts.get(1).getUser(), carts.get(1).getProduct().getStore()));
            Payment payment = paymentRepository.findByUserIdAndStoreId(carts.get(1).getUser().getId(),
                    carts.get(1).getProduct().getStore().getId()).get();
            for (Cart cart : carts) {
                totalPrice += cart.getPrice();
                historyBuyRepository.save(new HistoryBuy(0L,payment,cart.getProduct(),cart.getQuantity()));
            }
            payment.setTotalPrice(totalPrice);
            payment.setDateCreated(LocalDate.now());
            paymentRepository.save(payment);
            cartRepository.deleteAllCartByUserId(userId);

            User userSeller = userRepository.findById(carts.get(1).getProduct().getStore().getUser().getId()).get();
            User userBuyer = userRepository.findById(userId).get();
            userSeller.setWallet(userSeller.getWallet()+totalPrice);
            userBuyer.setWallet(userBuyer.getWallet()-totalPrice);
            userRepository.save(userSeller);
            userRepository.save(userBuyer);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
