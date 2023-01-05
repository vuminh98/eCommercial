package com.example.comercial.service.admin;

import com.example.comercial.model.User;
import com.example.comercial.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private IUserRepository userRepository;

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void activeBlockUser(Long id, Integer status) {
       findUserById(id).get().setStatus(status);
    }
}
