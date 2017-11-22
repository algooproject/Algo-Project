package com.algotrading.interactivebroker.test;

import static com.algotrading.interactivebroker.test.ToStringUtil.strTickAttr;

import java.util.Date;

//RealTimeData.java    
//API Version 9.72 and newer
//Version 1.3
//20170506
//R. Holowczak

//Import Java utilities and Interactive Brokers API
// http://interactivebrokers.github.io/#
// https://www.interactivebrokers.com/en/index.php?f=5041
import java.util.Vector;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.TagValue;
import com.ib.client.TickAttr;
import com.ib.client.TickType;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;

//RealTimeData Class is an implementation of the 
//IB API EWrapper class
public class RealTimeData extends BaseEWrapper {
	// Add for API 9.72 and newer
	private EJavaSignal m_signal = new EJavaSignal();
	private EReader m_reader;

	// Keep track of the next ID
	private int nextOrderID = 0;
	// The IB API Client Socket object
	private EClientSocket client = null;
	// Keep track of prices for Moving Average
	private double priceTotal;
	private int numberOfPrices;

	public RealTimeData() {
		// Initialize to 0
		priceTotal = 0.0;
		numberOfPrices = 0;
		// Create a new EClientSocket object version 9.71
		// client = new EClientSocket (this);
		client = new EClientSocket(this, m_signal);
		// Connect to the TWS or IB Gateway application
		// Leave null for localhost
		// Port Number (should match TWS/IB Gateway configuration
		client.eConnect(null, 7497, 0);

		// Pause here for connection to complete
		try {
			// Thread.sleep (1000);
			while (!(client.isConnected())) {
				System.out.println(new Date());
			}
			;
			// Can also try: while (client.NextOrderId <= 0);
		} catch (Exception e) {
		}
		;

		// API Version 9.72 and later Launch EReader Thread
		m_reader = new EReader(client, m_signal);
		m_reader.start();
		new Thread() {
			@Override
			public void run() {
				processMessages();
			}
		}.start();

		// Create a new contract
		// Contract contract = new Contract();
		// contract.symbol("ES");
		// // contract.expiry("20160318");
		// contract.lastTradeDateOrContractMonth("20160318");
		// contract.exchange("GLOBEX");
		// contract.secType("FUT");
		// contract.currency("USD");

		Contract contract = new Contract();
		contract.symbol("IBKR");
		contract.secType("STK");
		contract.currency("USD");
		// In the API side, NASDAQ is always defined as ISLAND in the exchange
		// field
		contract.exchange("ISLAND");

		Order order = OrderUtil.createOrder("", Action.BUY, OrderType.LMT, TimeInForce.DAY, 1, 55.39);
		// Order order = getOrder();
		client.placeOrder(nextOrderID++, contract, order);
		client.placeOrder(nextOrderID++, contract, getMarketDayOrder());

		// Create a TagValue list
		Vector<TagValue> mktDataOptions = new Vector<TagValue>();
		// Make a call to start off data retrieval
		client.reqMarketDataType(3);
		client.reqMktData(0, contract, null, false, false, mktDataOptions);
		// For API Version 9.73 and higher, add one more parameter: regulatory
		// snapshot
		// client.reqMktData(0, contract, null, false, false, mktDataOptions);

		// At this point our call is done and any market data events
		// will be returned via the tickPrice method

	} // end RealTimeData

	private void processMessages() {
		while (true) {
			try {
				// System.out.println("processing..." + new Date());
				m_reader.processMsgs();
			} catch (Exception e) {
				error(e);
			}
		} // end while
	} // end processMessages()

	/**
	 * Return the next valid OrderID
	 */
	@Override
	public void nextValidId(int orderId) {
		nextOrderID = orderId;
	}

	@Override
	public void tickPrice(int orderId, int field, double price, TickAttr tickAttr) {
		System.out.println("tickPrice(): orderId=" + orderId + ", field=" + field + "(" + TickType.getField(field)
				+ "), price=" + price + ", tickAttr=" + strTickAttr(tickAttr));
	}

	public void tickPrice(int orderId, int field, double price, int canAutoExecute) {
		double movingAverage = 0.0;
		try {
			// Print out the current price
			// field will provide the price type:
			// 1 = bid, 2 = ask, 4 = last
			// 6 = high, 7 = low, 9 = close
			if (field == 4) {
				numberOfPrices++;
				priceTotal += price;
				movingAverage = priceTotal / numberOfPrices;
				System.out.println("tickPrice: " + orderId + "," + field + "," + price + ", " + movingAverage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void tickSize(int orderId, int field, int size) {
		// TickType: 0=bid size, 3=ask size, 5=last size, 8=volume
		System.out.println("tickSize(): orderId=" + orderId + ", field=" + field + "(" + TickType.getField(field)
				+ "), size=" + size);
	}

	public static void main(String args[]) {
		try {
			// Create an instance
			// At this time a connection will be made
			// and the request for market data will happen
			RealTimeData myData = new RealTimeData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end main

	private Order getMarketDayOrder() {
		Order order = new Order();
		order.action(Action.BUY);
		order.orderType(OrderType.MKT);
		order.tif(TimeInForce.DAY);
		order.orderRef("");
		order.totalQuantity(1);
		// order.lmtPrice(55);
		return order;
	}

} // end public class RealTimeData