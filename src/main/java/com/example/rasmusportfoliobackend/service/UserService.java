package com.example.rasmusportfoliobackend.service;

import com.example.rasmusportfoliobackend.entity.User;
import com.example.rasmusportfoliobackend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;


@Component
public class UserService{

    @Autowired
    private UserRepo userRepository;

    private User user;

    public UserService() {
        this.user = null;
    }

    public void hashPasswordAndSave(User users){
        String password = users.getPassword();
        String hashed = DigestUtils.sha256Hex(password);
        users.setPassword(hashed);
        userRepository.save(users);
    }

    private String hashValidation(String password){
        return DigestUtils.sha256Hex(password);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User users) {
        this.user = users;
    }

    public Optional<User> fetchOptionalUser(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
}
