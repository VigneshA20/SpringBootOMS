package com.example.demo.ServiceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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



	@Override
	public User updateUserProfile(User user) {

		User dbUser = userRepository.findById(user.getId()).get();

		

		if (!ObjectUtils.isEmpty(dbUser)) {

			dbUser.setName(user.getName());
			dbUser.setMobileNumber(user.getMobileNumber());
			dbUser.setAddress(user.getAddress());
			dbUser.setCity(user.getCity());
			dbUser.setState(user.getState());
			dbUser.setPincode(user.getPincode());
			dbUser = userRepository.save(dbUser);
		}

		
		return dbUser;
	}





	
	

}
