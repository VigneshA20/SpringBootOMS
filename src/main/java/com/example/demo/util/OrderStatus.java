package com.example.demo.util;

public enum OrderStatus {
	
	
	IN_PROGRESS(1, "In Progress"),
	ORDER_RECEIVED(2, "Order Received"),
	PRODUCT_SHIPPED(3, "Product Shipped"),
	OUT_FOR_DELIVERY(4, "Out for Delivery"),
	DELIVERED(5, "Delivered"),
	CANCEL(6, "Cancelled"),
	SUCCESS(7, "Successfully Delivered");
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Integer id;
	
	private String name;
	
	private OrderStatus(Integer id, String name)
	{
		this.id = id;
		this.name = name;
	}

}
