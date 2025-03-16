package com.example.demo.controller;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private CartService cartService;
	
	
	
	
	@ModelAttribute
	public void getUserDetails(Principal p, Model m)
	{
		if(p!=null)
		{
			String email = p.getName();
			User user = userService.getUserByEmail(email);
			m.addAttribute("user", user);
			Integer countCart = cartService.getCountCart(user.getId());
			m.addAttribute("countCart", countCart);
		}
		
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
	}
	
	
	
	
	@GetMapping("/")  
	public String index()
	{
		return "index";
	}
	
	
	@GetMapping("/signin")
	public String login()
	{
		return "login";
	}
	
	
	@GetMapping("/register")
	public String register()
	{
		return "register";
	}
	

	@GetMapping("/products")
	public String products(Model m)
	{
		List<Category> categories = categoryService.getAllActiveCategory();
		List<Product> products = productService.getAllActiveProducts();
		m.addAttribute("categories", categories);
		m.addAttribute("products", products);
		return "products";
	}
	
	@GetMapping("/view_details/{id}")
	public String view_product(@PathVariable int id, Model m)
	{
		Product productById = productService.getProductById(id);
		m.addAttribute("product", productById);
		return "view_product";
	}
	
	
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, HttpSession session)
	{
		User saveUser = userService.saveUser(user);
		
		if(!ObjectUtils.isEmpty(saveUser))
		{
			session.setAttribute("successMsg", "User Registered Successfully");
		}
		else
		{
			session.setAttribute("errorMsg", " Internal Server Error");
		}
		return "redirect:/register";
	}
	
	
	
	

}
