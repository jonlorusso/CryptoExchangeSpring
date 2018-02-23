package com.jonlorusso.entity;

public class Order {
	private final Message message;
	
	private double quantity;
	private double price;
	private long timestamp;
	
	public Order(Message message) {
		this.message = message;
		_parseMessage(message.getMessage());
		this.timestamp = System.currentTimeMillis();
	}
	
	private void _parseMessage(String message) {
		String[] fields = message.split(" ");
		this.price = Double.parseDouble(fields[1]);
		this.quantity = Double.parseDouble(fields[2]);
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Message getMessage() {
		return message;
	}

	public double getPrice() {
		return price;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
