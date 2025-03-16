package com.example.demo.service;



import java.util.List;

import com.example.demo.model.User;

public interface UserService {
	
	
	public User saveUser(User user);
	
	public User getUserByEmail(String email);
	
	public List<User> getUsers(String role);


}
