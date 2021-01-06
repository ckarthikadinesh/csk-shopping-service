package com.csk.shopping.dto;

import java.util.ArrayList;
import java.util.List;

import com.csk.shopping.model.Cart;

public class Order {
	private int orderId;
	private String orderBy;
	private String orderStatus;
	private List<Cart> products = new ArrayList<>();

	public List<Cart> getProducts() {
		return products;
	}

	public void setProducts(List<Cart> products) {
		this.products = products;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}