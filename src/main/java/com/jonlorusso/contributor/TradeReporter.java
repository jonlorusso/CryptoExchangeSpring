package com.jonlorusso.contributor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jonlorusso.Contributor;
import com.jonlorusso.entity.Message;
import com.jonlorusso.entity.Message.MessageType;
import com.jonlorusso.entity.Trade;

@Component("TR")
public class TradeReporter extends Contributor {
	
	private List<Trade> trades = new ArrayList<>();
	
	public TradeReporter() {
		super("TR");
	}

	@Override
	public void receiveMessage(Message message) {
		super.receiveMessage(message);
		
		if (message.messageType == MessageType.BOT || message.messageType == MessageType.SLD ) {
			System.out.println(this.toString() + " reporting trade: " + message.toString());
			trades.add(new Trade(message));
		}
	}

	public List<Trade> getTrades() {
		return trades;
	}
}
