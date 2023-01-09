package com.example.comercial.service;

import com.example.comercial.model.HistoryBuy;
import com.example.comercial.model.Payment;
import com.example.comercial.model.Store;
import com.example.comercial.model.User;
import com.example.comercial.repository.IHistoryBuyRepository;
import com.example.comercial.repository.IPaymentRepository;
import com.example.comercial.repository.IStoreRepository;
import com.example.comercial.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    private IHistoryBuyRepository historyBuyRepository;
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private IStoreRepository storeRepository;
    @Autowired
    private IUserRepository userRepository;

    public Iterable<HistoryBuy> paymentDetails(Long paymentId){
        List<HistoryBuy> historyBuys = (List<HistoryBuy>) historyBuyRepository.findAllByPaymentId(paymentId);
        if(historyBuys.size()>0){
            return historyBuys;
        }else {
            return  null;
        }
    }
    public Iterable<Payment> paymentsBySearch(String key,String value,String storeId){
        Iterable<Payment> payments = paymentRepository.findAllByStoreId(Long.parseLong(storeId));
        switch (key) {
            case "phone":
                Optional<User> user = userRepository.findByPhone(value);
                if (user.isPresent()) {
                    payments = paymentRepository.findAllByUserPhoneAndStoreId(value, Long.parseLong(storeId));
                }
                break;
            case "totalPrice":
                payments = paymentRepository.findAllByTotalPriceBetween(Double.parseDouble(value) - 50, Double.parseDouble(value) + 50);
                break;
            case "dateCreated":
                payments = paymentRepository.findAllByDateCreatedAndStoreId(LocalDate.parse(value), Long.parseLong(storeId));
                break;
        }
        return payments;

    }
    public Iterable<Payment> paymentByOneUser(Long userId,Long storeId){
        List<Payment> payments = (List<Payment>) paymentRepository.findAllByStoreIdAndUserId(storeId, userId);
        if(payments.size()>0){
            return payments;
        }else {
            return  null;
        }
    }
    public Iterable<Payment> paymentByStore(Long storeId){
        Optional<Store> store = storeRepository.findById(storeId);
        if (store.isPresent()) {
            return paymentRepository.findAllByStoreId(storeId);
        }else {
            return null;
        }
    }
}