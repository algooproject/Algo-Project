package com.algotrading.interactivebroker.handler;

import com.algotrading.interactivebroker.Requester;
import com.algotrading.interactivebroker.builder.OrderBuilder;
import com.algotrading.persistence.mongo.helper.MongoDBHelper;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.TickAttr;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;

public class MustBuyTickPriceHandler implements TickPriceHandler {

	@Override
	public void handle(Requester requester, Contract contract, int field, double price, TickAttr attribs,
			MongoDBHelper dbHelper) {
		Order order = new OrderBuilder().action(Action.BUY)
				.orderType(OrderType.LMT)
				.tif(TimeInForce.DAY)
				.totalQuantity(1)
				.lmtPrice(price)
				.build();
		requester.placeOrder(contract, order);
	}

}
