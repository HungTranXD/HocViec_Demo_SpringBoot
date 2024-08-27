package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.NameOnly;
import com.example.springbootdemo.entity.User;
import com.example.springbootdemo.service.UserService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<NameOnly> getUsersName() {
        return userService.findAllName();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long id) {
        return userService.findById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getDeletedUsers() {
        return userService.findDeleted();
    }

    @PutMapping("/restore/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void restoreUser(@PathVariable Long id) {
        userService.restore(userService.findById(id).orElse(null));
    }

}
