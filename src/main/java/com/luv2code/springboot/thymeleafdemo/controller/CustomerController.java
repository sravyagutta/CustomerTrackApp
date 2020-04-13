package com.luv2code.springboot.thymeleafdemo.controller;

import java.util.List;


import javax.validation.Valid;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springboot.thymeleafdemo.entity.Customer;
import com.luv2code.springboot.thymeleafdemo.service.CustomerService;

@Controller

@RequestMapping("/customers")
public class CustomerController {

	private CustomerService customerService;
	
	public CustomerController(CustomerService theCustomerService) {
		customerService = theCustomerService;
	}
	
	// add mapping for "/list"

	@GetMapping("/list")
	public String listCustomers(Model theModel) {
		
		// get employees from db
		List<Customer> theCustomers = customerService.findAll();
		
		// add to the spring model
		theModel.addAttribute("customers", theCustomers);
		
		return "customers/list-customers";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		// create model attribute to bind form data
		Customer theCustomer = new Customer();
		
		theModel.addAttribute("customer", theCustomer);
		
		return "customers/customer-form";
	}

	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int theId,
									Model theModel) {
		
		// get the employee from the service
		Customer theCustomer = customerService.findById(theId);
		
		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("customer", theCustomer);
		
		// send over to our form
		return "customers/customer-form";			
	}
	
	
	@PostMapping("/save")
	public String saveCustomer( @Valid @ModelAttribute("customer")  Customer theCustomer,
			BindingResult result,Model model) {
		
	
		if(result.hasErrors()) {
			return "customers/customer-form";
		}
		// save the employee
		
			customerService.save(theCustomer);
		
		
		// use a redirect to prevent duplicate submissions
		return "redirect:/customers/list";
	
	
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("customerId") int theId){
		customerService.deleteById(theId);
		return "redirect:/customers/list";
		
	}
	
	
}












