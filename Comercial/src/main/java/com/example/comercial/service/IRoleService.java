package com.example.comercial.service;

import com.example.comercial.model.Role;

import java.util.List;

public interface IRoleService {
    List<Role> findAllRole();
    Role save(Role role);
}
