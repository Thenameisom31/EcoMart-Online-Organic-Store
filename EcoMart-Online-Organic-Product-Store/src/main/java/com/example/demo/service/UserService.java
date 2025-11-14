package com.example.demo.service;

import com.example.demo.entity.User;
import java.util.List;

public interface UserService {

	public boolean doResiter(User user);
	
	public User dologin(String gmail, String password);

	public User findByEmail(String email);

	long countAllUsers();

	public List<User> getAllUsers();
	
	public User updateUser(User user);
}
