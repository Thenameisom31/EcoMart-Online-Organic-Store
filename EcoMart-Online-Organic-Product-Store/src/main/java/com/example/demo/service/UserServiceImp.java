package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean doResiter(User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return false;
            }
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User dologin(String gmail, String password) {
        User valid = userRepository.findByEmail(gmail);

        if (valid != null && valid.getPassword().equals(password)) {
            return valid;
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Dashboard count
    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    // ✅ Corrected method
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
