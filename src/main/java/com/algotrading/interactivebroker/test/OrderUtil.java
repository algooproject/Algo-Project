package com.algotrading.interactivebroker.test;

import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;

public class OrderUtil {
	public static Order createOrder(String orderRef, Action action, OrderType orderType, TimeInForce timeInForce,
			double totalQuantity, double lmtPrice) {
		Order order = new Order();
		order.orderRef(orderRef);
		order.action(action);
		order.orderType(orderType);
		order.tif(timeInForce);
		order.totalQuantity(totalQuantity);
		order.lmtPrice(lmtPrice);
		return order;
	}
}
