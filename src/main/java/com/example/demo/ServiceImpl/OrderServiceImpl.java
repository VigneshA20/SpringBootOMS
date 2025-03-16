package com.example.demo.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.OrderAddress;
import com.example.demo.model.OrderRequest;
import com.example.demo.model.ProductOrder;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductOrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;


@Service
public class OrderServiceImpl implements OrderService {

	
	
	@Autowired
	private ProductOrderRepository orderRepository; 
	
	
	
	@Autowired
	private CartRepository cartRepository; 
	
	
	@Autowired
	private CommonUtil commomUtil;
	
	
	@Override
	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {
		
		
		
		
		List<Cart> carts = cartRepository.findByUserId(userid);

		for (Cart cart : carts) {

			ProductOrder order = new ProductOrder();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);

			ProductOrder saveOrder = orderRepository.save(order);
			commomUtil.sendMailForProductOrder(saveOrder, "Success");
	}

}


	@Override
	public List<ProductOrder> getOrderByUser(Integer userId) {
		
		
		
		List<ProductOrder> orders = orderRepository.findByUserId(userId);
		return orders;
	}


	@Override
	public List<ProductOrder> getAllOrders() {
	return orderRepository.findAll();

	}


	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		
		Optional<ProductOrder> findById = orderRepository.findById(id);
		if(findById.isPresent())
		{
			ProductOrder productOrder = findById.get();
			productOrder.setStatus(status);
			ProductOrder updateOrder = orderRepository.save(productOrder);
			return updateOrder;
		}
		
		return null;
	}
	
}
