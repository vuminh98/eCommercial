package com.example.comercial.controller;

import com.example.comercial.model.Role;
import com.example.comercial.model.User;
import com.example.comercial.service.impl.RoleService;
import com.example.comercial.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        return new ResponseEntity<>(userService.findById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>("Delete done!", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> create(@RequestBody User user) {
        Optional<User> checkUser = userService.findByUsername(user.getUsername());
        if (!checkUser.isPresent()) {
            Role role = roleService.findByName("ROLE_BUYER");
            user.setRoles(new HashSet<>());
            user.getRoles().add(role);
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id) {
        User userUpdate = userService.findById(id).get();
        if (userUpdate != null) {
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id={id}&status_pending")
    public ResponseEntity<User> activeBlockUser(@PathVariable Long id) {
        User userUpdate = userService.findById(id).get();
        if (userUpdate != null) {
           userUpdate.setStatus(2);
           return new ResponseEntity<>(userService.save(userUpdate), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
