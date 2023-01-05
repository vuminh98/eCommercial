package com.example.comercial.service.impl;

import com.example.comercial.model.User;
import com.example.comercial.repository.IUserRepository;
import com.example.comercial.service.ICrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService implements ICrudService<User, Long> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return userRepository.findById(aLong);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    public Optional<User> findByUsername(String name){
        return userRepository.findByUsername(name);
    }
}
