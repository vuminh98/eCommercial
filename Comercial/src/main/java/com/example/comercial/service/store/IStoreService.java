package com.example.comercial.service.store;

import com.example.comercial.model.login.User;
import com.example.comercial.model.product.Store;
import com.example.comercial.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStoreService extends IGeneralService<Store> {
    Page<Store> findAllByNameStoreContaining(String name, Pageable pageable);
}
