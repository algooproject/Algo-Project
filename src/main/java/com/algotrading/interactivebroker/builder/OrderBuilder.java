package com.algotrading.interactivebroker.builder;

import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;

public class OrderBuilder {

	private final Order order;

	public OrderBuilder() {
		order = new Order();
	}

	public Order build() {
		return order;
	}

	public OrderBuilder orderRef(String orderRef) {
		order.orderRef(orderRef);
		return this;
	}

	public OrderBuilder action(Action action) {
		order.action(action);
		return this;
	}

	public OrderBuilder orderType(OrderType orderType) {
		order.orderType(orderType);
		return this;
	}

	public OrderBuilder tif(TimeInForce tif) {
		order.tif(tif);
		return this;
	}

	public OrderBuilder totalQuantity(double totalQuantity) {
		order.totalQuantity(totalQuantity);
		return this;
	}

	public OrderBuilder lmtPrice(double lmtPrice) {
		order.lmtPrice(lmtPrice);
		return this;
	}

}
