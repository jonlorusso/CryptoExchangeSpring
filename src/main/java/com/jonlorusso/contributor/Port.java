package com.jonlorusso.contributor;

import com.jonlorusso.Contributor;
import com.jonlorusso.entity.Message;

public class Port extends Contributor {	
	public Port(String id) {
		super(id);
	}
	
	@Override
	public void receiveMessage(Message message) {
		if (message.getTopic() == this.id)
			super.receiveMessage(message);
	}
}
