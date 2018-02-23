package com.jonlorusso.contributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.jonlorusso.Contributor;
import com.jonlorusso.entity.Message;
import com.jonlorusso.entity.Message.MessageType;
import com.jonlorusso.entity.Order;
import com.jonlorusso.entity.Trade;

@Component("ME")
public class MatchingEngine extends Contributor {
	
	private static class OrderComparator implements Comparator<Order> {
		private boolean ascending = false;
		
		public OrderComparator(boolean ascending) {
			this.ascending = ascending;
		}

		@Override
		public int compare(Order o1, Order o2) {
			if (o1.getPrice() == o2.getPrice()) 
				return (int)(o1.getTimestamp() - o2.getTimestamp());
			
			if (this.ascending) {
				return o1.getPrice() > o2.getPrice() ? +1 : o1.getPrice() < o2.getPrice() ? -1 : 0;
			}
			
			return o1.getPrice() < o2.getPrice() ? +1 : o1.getPrice() > o2.getPrice() ? -1 : 0;
		}
	}
	
	private SortedSet<Order> buyOrders = new TreeSet<>(new OrderComparator(false));
	private SortedSet<Order> sellOrders = new TreeSet<>(new OrderComparator(true));
	
	private Map<String, Message> messages = new HashMap<>();
	
	public MatchingEngine() {
		super("ME");
	}
	
	private void cancelOrder(Message message) {
		String sequenceNumber = message.getMessage().split(" ")[1];
		Message original = messages.remove(message.getTopic() + "-" + sequenceNumber);
		
		if (original != null) {
			Order order = new Order(original);
			
			if (original.messageType == MessageType.BUY) {
				buyOrders.remove(order);
			} else if (original.messageType == MessageType.SEL) {
				sellOrders.remove(order);
			}
			
			System.out.println(this + " cancelled order " + original);	
		}
	}
	
	private void addOrder(Message message) {
		messages.put(message.getTopic() + "-" + message.getSequenceNumber(), message);
		System.out.println(this + " added order " + message);
		
	}
	
	private void matchSellOrder(Message message) {
		Order sellOrder = new Order(message);
		
		Iterator<Order> orders = buyOrders.iterator();
		while (orders.hasNext()) {
			Order buyOrder = orders.next();
		    if (sellOrder.getPrice() <= buyOrder.getPrice()) {
		    		Trade trade = new Trade(buyOrder, sellOrder);
		    		this.publishMessage(buyOrder.getMessage().getSource().id, "BOT " + trade.getPrice() + " " + trade.getQuantity());
		    		this.publishMessage(message.getSource().id, "SLD " + trade.getPrice() + " " + trade.getQuantity());
		    		
		    		buyOrder.setQuantity( buyOrder.getQuantity() - trade.getQuantity());
		    		if (buyOrder.getQuantity() == 0) {
		    			messages.remove(buyOrder.getMessage().getTopic() + "-" + buyOrder.getMessage().getSequenceNumber());
		    			orders.remove();
		    		}
		    		
		    		sellOrder.setQuantity(sellOrder.getQuantity() - trade.getQuantity());
		    		if (sellOrder.getQuantity() == 0) {
		    			break;
		    		}
		    }
		}
		
		// if some quantity remaining, add to order book
		if (sellOrder.getQuantity() > 0) {
			sellOrders.add(sellOrder);
			addOrder(message);
		}
	}
    
	private void matchBuyOrder(Message message) {
		Order buyOrder = new Order(message);

		Iterator<Order> orders = sellOrders.iterator();
		while (orders.hasNext()) {
			Order sellOrder = orders.next();
		    if (buyOrder.getPrice() >= sellOrder.getPrice()) {
		    		Trade trade = new Trade(sellOrder, buyOrder);
		    		this.publishMessage(sellOrder.getMessage().getSource().id, "SLD " + trade.getPrice() + " " + trade.getQuantity());
		    		this.publishMessage(message.getSource().id, "BOT " + trade.getPrice() + " " + trade.getQuantity());
		    		
		    		sellOrder.setQuantity(sellOrder.getQuantity() - trade.getQuantity());
		    		if (sellOrder.getQuantity() == 0) {
		    			messages.remove(sellOrder.getMessage().getTopic() + "-" + sellOrder.getMessage().getSequenceNumber());
		    			orders.remove();
		    		}
		    		
		    		buyOrder.setQuantity(buyOrder.getQuantity() - trade.getQuantity());
		    		if (buyOrder.getQuantity() == 0) {
		    			break;
		    		}
		    }
		}
		
		// if some quantity remaining, add to order book
		if (buyOrder.getQuantity() > 0) {
			buyOrders.add(buyOrder);
			addOrder(message);
		}
	}

	
	@Override
	public void receiveMessage(Message message) {
		super.receiveMessage(message);
		
		switch (message.messageType) {
			case BUY:
				this.matchBuyOrder(message);
				break;
			case SEL:
				this.matchSellOrder(message);
				break;
			case CA:
				this.cancelOrder(message);
				break;
			case SLD:
			case BOT:
				break;
		}
	}
	
	public List<Order> orderBook() {
		List<Order> orders = new ArrayList<Order>(sellOrders);
		Collections.sort(orders, new OrderComparator(false));
		orders.addAll(buyOrders);
		return orders;
	}
}
