package com.luv2code.springboot.thymeleafdemo.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.thymeleafdemo.dao.RoleRepository;
import com.luv2code.springboot.thymeleafdemo.dao.UserRepository;
import com.luv2code.springboot.thymeleafdemo.entity.Role;
import com.luv2code.springboot.thymeleafdemo.entity.User;

@Service
public class UserServiceImpl  implements UserService{

	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void saveUser(User user) {
		
		//** to save the user in to database we need repository class for user and role **
		
		// we are storing password using passwordencoder
		//so we need to autowire bcryptpassword encoder intho this class
		
		//we also need to import the user and role repositories : autowire the repositories
		
		
		//we changed the plain password into bcrypt
		user.setPassword(encoder.encode(user.getPassword()));
	
		//given a verified status to the user : only then he can register
		user.setStatus("Verified");
		
		// and we created the role for the user as the site user.
		//we are using roleRepository here
		Role userRole= roleRepository.findByRole("SITE_USER");
		
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		
		
		//and saving the user in the repository by calling the UserRepository.
		userRepository.save(user);
		
	}

	@Override
	public boolean isUserAlreadyPresent(String email) {
		
		User u =userRepository.findByEmail(email);
			
			if(u!=null)
				return true;
			
		return false;
	}

	
}
