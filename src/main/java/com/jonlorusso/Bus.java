package com.jonlorusso;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.jonlorusso.entity.Message;

@Component
public class Bus {
	private ArrayList<Contributor> subscribers = new ArrayList<>();
	
	public void register(Contributor contributor) {
		subscribers.add(contributor);
	}
	
	/*
	 * FIXME: Messages are not received simultaneously
	 */
	public void publishMessage(Message message) {
		for ( Contributor contributor : subscribers ) {
			if ( message.getSource() != contributor) {
				contributor.receiveMessage(message);
			}
		}
	}
}
