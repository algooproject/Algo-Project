package com.algotrading.interactivebroker.test;

import com.algotrading.interactivebroker.builder.ContractBuilder;
import com.algotrading.interactivebroker.builder.OrderBuilder;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;

@Deprecated
public class DummyUtil {

	@Deprecated
	public static Order createTestLimitDayOrder() {
		return new OrderBuilder().orderRef("111")
				.action(Action.SELL)
				.orderType(OrderType.LMT)
				.tif(TimeInForce.DAY)
				.totalQuantity(1)
				.lmtPrice(55.39)
				.build();
	}

	@Deprecated
	public static Order createTestMarketDayOrder() {
		return new OrderBuilder().action(Action.BUY)
				.orderType(OrderType.MKT)
				.tif(TimeInForce.DAY)
				.orderRef("")
				.totalQuantity(1)
				.build();
	}

	@Deprecated
	public static Contract createContract() {
		return new ContractBuilder().symbol("IBKR")
				.secType("STK")
				.currency("USD")
				.exchange("ISLAND")
				.build();
	}

}
