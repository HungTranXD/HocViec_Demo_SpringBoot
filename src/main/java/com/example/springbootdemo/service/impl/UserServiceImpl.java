package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dto.NameOnly;
import com.example.springbootdemo.entity.User;
import com.example.springbootdemo.repository.UserRepository;
import com.example.springbootdemo.repository._GenericRepository;
import com.example.springbootdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends _GenericServiceImpl<User> implements UserService {

    public UserServiceImpl(_GenericRepository<User> repository) {
        super(repository);
    }

    @Override
    public User findByEmail(String email) {
        return ((UserRepository) repository).findByEmail(email);
    }

    @Override
    public List<NameOnly> findAllName() {
        return ((UserRepository) repository).findAllBy();
    }

}
