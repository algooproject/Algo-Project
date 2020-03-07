package com.algotrading.interactivebroker.handler;

import com.algotrading.interactivebroker.Requester;
import com.algotrading.interactivebroker.builder.OrderBuilder;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.TickAttr;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;

public class MustBuyTickPriceHandler implements TickPriceHandler {

	@Override
	public void handle(Requester requester, Contract contract, int field, double price, TickAttr attribs) {
//		Order order = new OrderBuilder().action(Action.BUY).orderType(OrderType.LMT).tif(TimeInForce.DAY)
//				.totalQuantity(50000).lmtPrice(price).build();
		Order order = new OrderBuilder().action(Action.BUY).orderType(OrderType.MKT).tif(TimeInForce.DAY)
				.totalQuantity(1000).build();

		requester.placeOrder(contract, order);
	}

}
