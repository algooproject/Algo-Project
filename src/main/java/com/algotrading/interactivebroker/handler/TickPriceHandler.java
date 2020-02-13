package com.algotrading.interactivebroker.handler;

import com.algotrading.interactivebroker.Requester;
import com.ib.client.Contract;
import com.ib.client.TickAttr;

public interface TickPriceHandler {
	void handle(Requester requester, Contract contract, int field, double price, TickAttr attribs);
}
