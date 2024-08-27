package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.NameOnly;
import com.example.springbootdemo.entity.User;

import java.util.List;

public interface UserService extends _GenericService<User, Long> {

    // Add additional methods here:
    User findByEmail(String email);
    List<NameOnly> findAllName();

}
