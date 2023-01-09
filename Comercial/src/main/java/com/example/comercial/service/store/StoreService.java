package com.example.comercial.service.store;

import com.example.comercial.model.product.Store;
import com.example.comercial.repository.store.IStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreService implements IStoreService {
    @Autowired
    private IStoreRepository storeRepository;

    @Override
    public Iterable<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public Iterable<Store> findByName(String name) {
        return null;
    }

    @Override
    public Page<Store> findAllPage(Pageable pageable) {
        return null;
    }

    @Override
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public boolean remove(Long id) {
        try {
            storeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Page<Store> findAllByNameStoreContaining(String name, Pageable pageable) {
        return storeRepository.findAllByNameStoreContaining(name, pageable);
    }
}
