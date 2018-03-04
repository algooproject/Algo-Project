package com.algotrading.interactivebroker;

import java.util.List;
import java.util.Map;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import com.ib.client.TagValue;

/**
 * for EClient to request. To separate it just because of placing javadoc.
 */
public class Requester {
	private final RealTimeData ibClient;
	private final EClientSocket clientSocket;
	private final Map<Integer, Contract> marketRequestMap;

	public Requester(RealTimeData ibClient, EClientSocket clientSocket, Map<Integer, Contract> marketRequestMap) {
		this.ibClient = ibClient;
		this.clientSocket = clientSocket;
		this.marketRequestMap = marketRequestMap;
	}

	/**
	 * Switches data type returned from reqMktData request to "frozen",
	 * "delayed" or "delayed-frozen" market data. Requires TWS/IBG v963+. The
	 * API can receive frozen market data from Trader Workstation. Frozen market
	 * data is the last data recorded in our system. During normal trading
	 * hours, the API receives real-time market data. Invoking this function
	 * with argument 2 requests a switch to frozen data immediately or after the
	 * close. When the market reopens the next data the market data type will
	 * automatically switch back to real time if available.
	 * 
	 * @param marketDataType
	 *            by default only real-time (1) market data is enabled sending 1
	 *            (real-time) disables frozen, delayed and delayed-frozen market
	 *            data sending 2 (frozen) enables frozen market data sending 3
	 *            (delayed) enables delayed and disables delayed-frozen market
	 *            data sending 4 (delayed-frozen) enables delayed and
	 *            delayed-frozen market data
	 */
	public synchronized void reqMarketDataType(int marketDataType) {
		clientSocket.reqMarketDataType(marketDataType);
	}

	/**
	 * Ka Wing: Also place the mapping of request id to contract
	 * 
	 * Requests real time market data. Returns market data for an instrument
	 * either in real time or 10-15 minutes delayed (depending on the market
	 * data type specified)
	 * 
	 * @param tickerId
	 *            the request's identifier
	 * @param contract
	 *            the Contract for which the data is being requested
	 * @param genericTickList
	 *            comma separated ids of the available generic ticks:
	 *            <li>100 Option Volume (currently for stocks)</li>
	 *            <li>101 Option Open Interest (currently for stocks)</li>
	 *            <li>104 Historical Volatility (currently for stocks)</li>
	 *            <li>106 Option Implied Volatility (currently for stocks)</li>
	 *            <li>162 Index Future Premium</li>
	 *            <li>165 Miscellaneous Stats</li>
	 *            <li>221 Mark Price (used in TWS P&L computations)</li>
	 *            <li>225 Auction values (volume, price and imbalance)</li>
	 *            <li>233 RTVolume - contains the last trade price, last trade
	 *            size, last trade time, total volume, VWAP, and single trade
	 *            flag.</li>
	 *            <li>236 Shortable</li>
	 *            <li>256 Inventory</li>
	 *            <li>258 Fundamental Ratios</li>
	 *            <li>411 Realtime Historical Volatility</li>
	 *            <li>456 IBDividends</li>
	 * @param snapshot
	 *            for users with corresponding real time market data
	 *            subscriptions. A true value will return a one-time snapshot,
	 *            while a false value will provide streaming data.
	 * @param regulatory
	 *            snapshot for US stocks requests NBBO snapshots for users which
	 *            have "US Securities Snapshot Bundle" subscription but not
	 *            corresponding Network A, B, or C subscription necessary for
	 *            streaming * market data. One-time snapshot of current market
	 *            price that will incur a fee of 1 cent to the account per
	 *            snapshot.
	 * @param marketDataOptions
	 *            unknown
	 */
	public synchronized void reqMktData(int tickerId, Contract contract, String genericTickList, boolean snapshot,
			boolean regulatory, List<TagValue> marketDataOptions) {
		marketRequestMap.put(tickerId, contract);
		clientSocket.reqMktData(tickerId, contract, genericTickList, snapshot, regulatory, marketDataOptions);
	}

	/**
	 * Requests historical Time&Sales data for an instrument.
	 * 
	 * @param reqId
	 *            id of the request
	 * @param contract
	 *            Contract object that is subject of query
	 * @param startDateTime
	 *            i.e. "20170701 12:01:00". Uses TWS timezone specified at
	 *            login.
	 * @param endDateTime
	 *            i.e. "20170701 13:01:00". In TWS timezone. Exactly one of
	 *            start time and end time has to be defined.
	 * @param numberOfTicks
	 *            Number of distinct data points. Max currently 1000 per
	 *            request.
	 * @param whatToShow
	 *            (Bid_Ask, Midpoint, Trades) Type of data requested.
	 * @param useRth
	 *            Data from regular trading hours (1), or all available hours
	 *            (0)
	 * @param ignoreSize
	 *            A filter only used when the source price is Bid_Ask
	 * @param miscOptions
	 *            should be defined as null, reserved for internal use
	 */
	public synchronized void reqHistoricalTicks(int reqId, Contract contract, String startDateTime, String endDateTime,
			int numberOfTicks, String whatToShow, int useRth, boolean ignoreSize, List<TagValue> miscOptions) {
		clientSocket.reqHistoricalTicks(reqId, contract, startDateTime, endDateTime, numberOfTicks, whatToShow, useRth,
				ignoreSize, miscOptions);
	}

	/**
	 * Subscribes to an specific account's information and portfolio Through
	 * this method, a single account's subscription can be started/stopped. As a
	 * result from the subscription, the account's information, portfolio and
	 * last update time will be received at EWrapper::updateAccountValue,
	 * EWrapper::updateAccountPortfolio, EWrapper::updateAccountTime
	 * respectively. All account values and positions will be returned
	 * initially, and then there will only be updates when there is a change in
	 * a position, or to an account value every 3 minutes if it has changed.
	 * Only one account can be subscribed at a time. A second subscription
	 * request for another account when the previous one is still active will
	 * cause the first one to be canceled in favour of the second one. Consider
	 * user reqPositions if you want to retrieve all your accounts' portfolios
	 * directly.
	 * 
	 * @param subscribe
	 *            set to true to start the subscription and to false to stop it.
	 * @param acctCode
	 *            the account id (i.e. U123456) for which the information is
	 *            requested.
	 */
	public synchronized void reqAccountUpdates(boolean subscribe, String acctCode) {
		clientSocket.reqAccountUpdates(subscribe, acctCode);
	}

	/**
	 * Requests the accounts to which the logged user has access to.
	 * 
	 * @see EWrapper::managedAccounts
	 */
	public synchronized void reqManagedAccts() {
		clientSocket.reqManagedAccts();
	}

	public void placeOrder(Contract contract, Order order) {
		clientSocket.placeOrder(ibClient.getNextOrderId(), contract, order);
	}
}
