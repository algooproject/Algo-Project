package com.algotrading.interactivebroker.builder;

import org.junit.Assert;
import org.junit.Test;

import com.algotrading.interactivebroker.builder.OrderBuilder;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;

public class OrderBuilderTest {

	@Test
	public void testOrderBuilder() {
		Order order = new OrderBuilder().orderRef("111")
				.action(Action.SELL)
				.orderType(OrderType.LMT)
				.tif(TimeInForce.DAY)
				.totalQuantity(1)
				.lmtPrice(55.39)
				.build();
		Assert.assertEquals("111", order.orderRef());
		Assert.assertEquals(Action.SELL, order.action());
		Assert.assertEquals(OrderType.LMT, order.orderType());
		Assert.assertEquals(TimeInForce.DAY, order.tif());
		Assert.assertEquals(1, order.totalQuantity(), 0.01);
		Assert.assertEquals(55.39, order.lmtPrice(), 0.01);
	}

}
