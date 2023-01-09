package com.example.comercial.service.cart;

import com.example.comercial.model.Cart;
import com.example.comercial.model.HistoryBuy;
import com.example.comercial.model.Payment;
import com.example.comercial.model.User;
import com.example.comercial.model.product.Product;
import com.example.comercial.repository.*;
//import com.example.comercial.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
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
//    @Autowired
//    private UserService userService;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;


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
            Payment payment = paymentRepository.findByUserIdAndStoreIdAndStatus(carts.get(0).getUser().getId(),
                    carts.get(0).getProduct().getStore().getId(),false).get();
            Product product ;
            for (Cart cart : carts) {
                totalPrice += cart.getPrice();
                historyBuyRepository.save(new HistoryBuy(0L,payment,cart.getProduct(),cart.getQuantity()));
                product = productRepository.findById(cart.getProduct().getId()).get();
                product.setQuantity(product.getQuantity() - cart.getQuantity());
                productRepository.save(product);
            }
            payment.setTotalPrice(totalPrice);
            paymentRepository.save(payment);
//            userService.payment(userId,carts.get(0).getProduct().getStore().getUser().getId(),totalPrice);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean accept(Long paymentId){
        try{
            Optional<Payment> payment = paymentRepository.findById(paymentId);
            if(payment.isPresent()){
                cartRepository.deleteAllCartByUserId(payment.get().getUser().getId());
                payment.get().setStatus(true);
                paymentRepository.save(payment.get());
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    public boolean deletePayment(Long id){
        try{
            Optional<Payment> payment = paymentRepository.findById(id);
            if(payment.isPresent()){
                Iterable<HistoryBuy> historyBuys = historyBuyRepository.findAllByPaymentId(payment.get().getId());
                for(HistoryBuy historyBuy : historyBuys){
                    historyBuyRepository.deleteById(historyBuy.getId());
                }
                User userBuyer = userRepository.findById(payment.get().getUser().getId()).get();
                User userSeller = userRepository.findById(payment.get().getStore().getUser().getId()).get();
                userBuyer.setWallet(userBuyer.getWallet()+payment.get().getTotalPrice());
                userSeller.setWallet(userSeller.getWallet()-payment.get().getTotalPrice());
                userRepository.save(userBuyer);
                userRepository.save(userSeller);
                paymentRepository.deleteById(payment.get().getId());
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
}
