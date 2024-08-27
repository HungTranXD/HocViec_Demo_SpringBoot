package com.example.springbootdemo.repository;

import com.example.springbootdemo.dto.NameOnly;
import com.example.springbootdemo.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends _GenericRepository<User> {
    User findByEmail(String email);
    List<NameOnly> findAllBy();
}
