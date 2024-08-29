package com.example.springbootdemo.repository;

import com.example.springbootdemo.dto.user.NameOnly;
import com.example.springbootdemo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends _GenericRepository<User> {
    User findByEmail(String email);
    List<NameOnly> findAllBy();
}
