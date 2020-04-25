package com.luv2code.springboot.thymeleafdemo.controller;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.luv2code.springboot.thymeleafdemo.entity.User;
import com.luv2code.springboot.thymeleafdemo.service.UserService;

@Controller
public class AuthenticationController {

	//we autowire userService class here so the registered user can be saved to the service
	//and then to the repository and then to the database
	 @Autowired
	 UserService userService ;
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login"); // resources/template/login.html
		return modelAndView;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView modelAndView = new ModelAndView();
		 User user = new User();
		modelAndView.addObject("user", user); 
		modelAndView.setViewName("register"); // resources/template/register.html
		return modelAndView;
	}
	
	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("customer-form"); // resources/template/home.html
		return modelAndView;
	}
	
	@RequestMapping(value ="/register" , method = RequestMethod.POST)
	public ModelAndView registerUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView();
		
		//check for validations
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("Message", "Please correct the errors in the form");
            modelMap.addAttribute("bindingResult",bindingResult);
		}
		
		
		
		else  if(userService.isUserAlreadyPresent(user.getEmail())){
			
			modelAndView.addObject("Message", "User already existts");
		
				
		}
		
		//we wl save the user if no errors
		else {
			
			//save the registration form
			//** we have to create the service class(userService is the service class )
			//to save the user **
			
			//we autowired userService  in this class ..so we can save the user to userService
		
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User is registered successfully");
		}
		
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("register");
		
		return modelAndView;
			
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView adminHome() {
		ModelAndView modelAndView = new ModelAndView();
		
		//loading the view from our controller method 
		modelAndView.setViewName("admin"); // resources/template/admin.html
		return modelAndView;
	}
}