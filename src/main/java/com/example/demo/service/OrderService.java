package com.example.demo.service;

import java.util.List;

import com.example.demo.model.OrderRequest;
import com.example.demo.model.ProductOrder;

public interface OrderService {

	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;
	
	public List<ProductOrder> getOrderByUser(Integer userId);
	
	public List<ProductOrder> getAllOrders();
	
	public ProductOrder updateOrderStatus(Integer id, String status);
	

}
