package com.example.demo.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	@Override
	public User saveUser(User user) {
		
		
		user.setRole("ROLE_USER");
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		
		 User saveUser = userRepository.save(user);
		 return saveUser;
	}



	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}



	@Override
	public List<User> getUsers(String role) {
		return userRepository.findByRole(role);	}




	
	

}
