package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Cart;
import com.example.demo.model.Category;
import com.example.demo.model.OrderRequest;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private CategoryService categoryService;
	
	
	@Autowired
	private CartService cartService;
	
	
	@Autowired
	private OrderService orderService;
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	
	@GetMapping("/")
	public String Home()
	{
		return "user/home";
	}
	
	
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
		m.addAttribute("category", allActiveCategory);
	}
	

	@GetMapping("addCart")
	public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session)
	{
		
		Cart saveCart = cartService.saveCart(pid, uid);
		
		if(ObjectUtils.isEmpty(saveCart))
		{
			session.setAttribute("errorMsg", "Product Add to Cart Failed");
		}
		else
		{
			session.setAttribute("successMsg", "Added to Cart");
		}
		return "redirect:/view_details/" + pid;
	}

	
	

	
	
	@GetMapping("/cart")
	public String loadCartPage(Principal p, Model m)
	{
		
		
		User user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartByUser(user.getId());
		m.addAttribute("carts", carts);
		
		if(carts.size() > 0)
		{
			Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
			m.addAttribute("totalOrderPrice", totalOrderPrice);
		}
			return "/user/cart";
	}
	
	
	
	
	
	@GetMapping("cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid)
	{
		cartService.updateQuantity(sy, cid);
		
		return "redirect:/user/cart";
	}
	
	
	
	
	
	
	
	


	private User getLoggedInUserDetails(Principal p) {
		

		String email = p.getName();
		User userDtls = userService.getUserByEmail(email);
		
		return userDtls;
	}
	
	
	
	
	
	
	@GetMapping("/orders")
	public String orderPage(Principal p, Model m)
	{
		User user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartByUser(user.getId());
		m.addAttribute("carts", carts);
		
		if(carts.size() > 0)
		{
			Double orderPrice = carts.get(carts.size()-1).getTotalOrderPrice();

			Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice() + 250 + 100;
			m.addAttribute("orderPrice", orderPrice);

			m.addAttribute("totalOrderPrice", totalOrderPrice);
		}
			
		return "/user/order";
	}
	
	
	
	@PostMapping("save-order")
	public String saveOrder(@ModelAttribute OrderRequest request, Principal p) throws Exception
	{
		User user = getLoggedInUserDetails(p);
		orderService.saveOrder(user.getId(), request);
		
		return "redirect:/user/success";
	}
	
	
	
	
	@GetMapping("/success")
	public String loadSuccess()
	{
		return "/user/success";

	}
	
	
	
	@GetMapping("/user-orders")
	public String myOrder(Model m, Principal p)
	{
		
		User loginUser = getLoggedInUserDetails(p);
		List<ProductOrder> orders = orderService.getOrderByUser(loginUser.getId());
		m.addAttribute("orders", orders);
		return "/user/my_order";

	}
	
	
	
	
	
	
	
	
	
	

	@GetMapping("/update-status")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session)
	{
		
		OrderStatus[] values = OrderStatus.values();
		String status = null;
		
		for(OrderStatus ordertSt : values)
		{
			if(ordertSt.getId().equals(st))
			{
				status = ordertSt.getName();
			}
		}
		
		
		ProductOrder updateOrder = orderService.updateOrderStatus(id, status);
		try {
			commonUtil.sendMailForProductOrder(updateOrder, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(!ObjectUtils.isEmpty(updateOrder))
		{
			session.setAttribute("successMsg", "Your Order is Cancelled");
		}
		else
		{
			session.setAttribute("errorMsg", "Internal Server Error");
		}
		
		return "redirect:/user/user-orders";

	}
	
	
	
	@GetMapping("/profile")
	public String profile()
	{
		return "/user/profile";
	}
	
	
	
	
	
	
	
	
	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute User user, HttpSession session) {
		User updateUserProfile = userService.updateUserProfile(user);
		if (ObjectUtils.isEmpty(updateUserProfile)) {
			session.setAttribute("errorMsg", "Profile not updated");
		} else {
			session.setAttribute("successMsg", "Profile Updated");
		}
		return "redirect:/user/profile";
	}

	
}
