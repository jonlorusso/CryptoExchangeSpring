package com.jonlorusso;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jonlorusso.contributor.MatchingEngine;
import com.jonlorusso.contributor.Port;
import com.jonlorusso.contributor.TradeReporter;
import com.jonlorusso.entity.Message;

@Controller
public class CryptoExchangeController {
	
	@Autowired
	private MatchingEngine matchingEngine;
	
	@Autowired
	private TradeReporter tradeReporter;
	
	@Autowired
	private Map<String, Port> ports;

    @RequestMapping("/orderbook")
    public String orderbook(Model model) {
    		model.addAttribute("orderBook", matchingEngine.orderBook());
    		return "orderbook";
    }
    
    @RequestMapping("/ports")
    public String ports(Model model) {
    		model.addAttribute("ports", ports);
    		return "ports";
    }
    
    @RequestMapping("/trades")
    public String trades(Model model) {
    		model.addAttribute("trades", tradeReporter.getTrades());
    		return "trades";
    }
    
    @RequestMapping("/publish")
    public String publish(@RequestParam(value="port", required=true) String portId, @RequestParam(value="message", required=true) String message, Model model) {
    		for (Port port : ports.values()) {
    			if (port.id.equals(portId)) {
    				Message publishedMessage = port.publishMessage(portId, message);
    		        model.addAttribute("message", publishedMessage);
    		        break;
    			}
    		}
    		return "publish";
    }
}
