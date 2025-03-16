package com.example.demo.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	
	@Autowired
	private CartRepository cartRepository;
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;


	@Override
	public Cart saveCart(Integer productId, Integer userId) {
	    // Fetch user and product from the database
	    User user = userRepository.findById(userId)
	                              .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
	    Product product = productRepository.findById(productId)
	                              .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

	    // Check if the product is already in the cart
	    Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);
	    
	    Cart cart;
	    if (cartStatus == null) {
	        // If product is not in the cart, add a new entry
	        cart = new Cart();
	        cart.setProduct(product);
	        cart.setUser(user);
	        cart.setQuantity(1);
	        cart.setTotalPrice(product.getPrice());
	    } else {
	        // If already in cart, increase quantity
	        cart = cartStatus;
	        cart.setQuantity(cart.getQuantity() + 1);
	        cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getPrice());
	    }
	    
	    // Save cart and return
	    return cartRepository.save(cart);
	}


	@Override
	public List<Cart> getCartByUser(Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		
		
		Double totalOrderPrice = 0.0;
		
		List<Cart> updateCarts = new ArrayList<>();
		
		for(Cart c : carts)
		{
			Double totalPrice = (c.getProduct().getPrice() * c.getQuantity());
			c.setTotalPrice(totalPrice);
			
			
			totalOrderPrice = totalOrderPrice + totalPrice;
			
			c.setTotalOrderPrice(totalOrderPrice);
			
			
			updateCarts.add(c);
		}
		
		
		
		
		
		return updateCarts;
	}


	@Override
	public Integer getCountCart(Integer userId) {
		
		Integer countByUserId = cartRepository.countByUserId(userId);
		
		return countByUserId;
	}


	
	
	
	
	@Override
	public void updateQuantity(String sy, Integer cid) {
		
		
		Cart cart = cartRepository.findById(cid).get();
		int updateQuantity;
		
		
		if(sy.equalsIgnoreCase("de"))
		{
			updateQuantity = cart.getQuantity() - 1;
			
			
			if(updateQuantity <= 0)
			{
				cartRepository.delete(cart);
			}
			else
			{
				cart.setQuantity(updateQuantity);
				cartRepository.save(cart);
			}
		}
		else
		{
			updateQuantity = cart.getQuantity() + 1;
			cart.setQuantity(updateQuantity);
			cartRepository.save(cart);
		}
		
		
	}
	

}
