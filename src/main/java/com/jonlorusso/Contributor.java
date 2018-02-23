package com.jonlorusso;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.jonlorusso.entity.Message;

public abstract class Contributor {

	public final String id;
	private Bus bus;
	private ArrayList<Message> receivedMessages = new ArrayList<>();
	
	private int lastSequenceNumber = 0;

	private Message lastSent;
	
	public Contributor(String id) {
		this.id = id;
	}

	@Autowired
	public void setBus(Bus bus) {
		this.bus = bus;
		bus.register(this);
	}
	
	public Message publishMessage(String topic, String messageText) {
		lastSequenceNumber += 1;
		this.lastSent = new Message(this, topic, this.lastSequenceNumber, messageText);
//		System.out.println(this.toString() + " published message " + this.lastSent.toString());
		this.bus.publishMessage(this.lastSent);	
		return this.lastSent;
	}

	public void receiveMessage(Message message) {
		receivedMessages.add(message);
	}

	public int getLastSequenceNumber() {
		return lastSequenceNumber;
	}

	@Override
	public String toString() {
		return "Contributor [id=" + id + "]";
	}	
}
