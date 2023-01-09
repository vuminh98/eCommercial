package com.example.comercial.service.cart;

import com.example.comercial.model.Cart;
import com.example.comercial.model.HistoryBuy;
import com.example.comercial.model.Payment;
import com.example.comercial.model.Review;
import com.example.comercial.repository.ICartRepository;
import com.example.comercial.repository.IHistoryBuyRepository;
import com.example.comercial.repository.IPaymentRepository;
import com.example.comercial.repository.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class PaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private IReviewRepository reviewRepository;
    public Iterable<Payment> findAllByUserId(long userId) {
        return paymentRepository.findAllByUserId(userId);
    }
    public boolean saveReviews(Review review){
        try{
            reviewRepository.save(review);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
