package com.luv2code.springboot.thymeleafdemo.service;

import com.luv2code.springboot.thymeleafdemo.entity.User;

public interface UserService {

	public void saveUser(User user);
	public boolean isUserAlreadyPresent(String email) ;
	
}
