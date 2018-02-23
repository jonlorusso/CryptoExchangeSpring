package com.jonlorusso.entity;

public class Trade {

	private String action; //FIXME
	private double quantity;
	private double price;

	public Trade(Order makerOrder, Order takerOrder) {
		this.quantity = Math.min(makerOrder.getQuantity(), takerOrder.getQuantity());
		this.price = makerOrder.getPrice();
	}

	public Trade(Message message) {
		String[] fields = message.getMessage().split(" ");
		this.action = fields[0];
		this.quantity = Double.valueOf(fields[1]);
		this.price = Double.valueOf(fields[2]);
	}
	
	public String getAction() {
		return action;
	}
	
	public double getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}
}
