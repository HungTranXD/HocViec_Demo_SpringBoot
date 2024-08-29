package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dto.user.NameOnly;
import com.example.springbootdemo.entity.User;
import com.example.springbootdemo.repository.UserRepository;
import com.example.springbootdemo.repository._GenericRepository;
import com.example.springbootdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl extends _GenericServiceImpl<User> implements UserService {

    public UserServiceImpl(_GenericRepository<User> repository) {
        super(repository);
    }

    @Override
    @Cacheable(value = "users", key = "#email", cacheManager = "concurrentMapCacheManager")
    public User findByEmail(String email) {
        log.info("Find user by email: {}", email);
        return ((UserRepository) repository).findByEmail(email);
    }

    @Override
    public List<NameOnly> findAllName() {
        return ((UserRepository) repository).findAllBy();
    }

}
