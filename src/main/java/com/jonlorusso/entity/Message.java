package com.jonlorusso.entity;

import com.jonlorusso.Contributor;

public class Message {
	
	public static enum MessageType { BUY, SEL, CA, BOT, SLD };
	
	public final MessageType messageType;
	private Contributor source;
	private  String topic;
	private int sequenceNumber;
	private String message;

	public Message(Contributor source, String topic, int sequenceNumber, String message) {
		this.source = source;
		this.topic = topic;
		this.sequenceNumber = sequenceNumber;
		this.message = message;
		this.messageType = MessageType.valueOf(message.split(" ")[0]);
	}

	@Override
	public String toString() {
		return this.source.id + ":" + this.topic + ":" + this.message;
	}
	
	public Contributor getSource() {
		return source;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public String getMessage() {
		return message;
	}
}
