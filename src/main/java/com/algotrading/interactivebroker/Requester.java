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
	 * Requests contracts' historical data. When requesting historical data, a
	 * finishing time and date is required along with a duration string. For
	 * example, having: - endDateTime: 20130701 23:59:59 GMT - durationStr: 3 D
	 * 
	 * @return three days of data counting backwards from July 1st 2013 at
	 *         23:59:59 GMT resulting in all the available bars of the last
	 *         three days until the date and time specified. It is possible to
	 *         specify a timezone optionally. The resulting bars will be
	 *         returned in EWrapper::historicalData
	 * 
	 * @param tickerId
	 *            the request's unique identifier. contract the contract for
	 *            which we want to retrieve the data. endDateTime request's
	 *            ending time with format yyyyMMdd HH:mm:ss {TMZ} durationString
	 *            the amount of time for which the data needs to be retrieved:
	 *            " S (seconds) - " D (days) " W (weeks) - " M (months) " Y
	 *            (years) barSizeSetting the size of the bar: 1 sec 5 secs 15
	 *            secs 30 secs 1 min 2 mins 3 mins 5 mins 15 mins 30 mins 1 hour
	 *            1 day whatToShow the kind of information being retrieved:
	 *            TRADES MIDPOINT BID ASK BID_ASK HISTORICAL_VOLATILITY
	 *            OPTION_IMPLIED_VOLATILITY FEE_RATE REBATE_RATE useRTH set to 0
	 *            to obtain the data which was also generated outside of the
	 *            Regular Trading Hours, set to 1 to obtain only the RTH data
	 *            formatDate set to 1 to obtain the bars' time as yyyyMMdd
	 *            HH:mm:ss, set to 2 to obtain it like system time format in
	 *            seconds keepUpToDate set to True to received continuous
	 *            updates on most recent bar data. If True, and endDateTime
	 *            cannot be specified.
	 *
	 * @see EWrapper::historicalData
	 */
	void reqHistoricalData(int tickerId, Contract contract, String endDateTime, String durationString,
			String barSizeSetting, String whatToShow, int useRTH, int formatDate, Boolean keepUpToDate,
			List<TagValue> chartOptions) {
		clientSocket.reqHistoricalData(tickerId, contract, endDateTime, durationString, barSizeSetting, whatToShow,
				useRTH, formatDate, keepUpToDate, chartOptions);
	}

	/**
	 * Requests historical news headlines.
	 * 
	 * @parm requestId conId - contract id of ticker providerCodes - a
	 *       '+'-separated list of provider codes startDateTime - marks the
	 *       (exclusive) start of the date range. The format is yyyy-MM-dd
	 *       HH:mm:ss.0 endDateTime - marks the (inclusive) end of the date
	 *       range. The format is yyyy-MM-dd HH:mm:ss.0 totalResults - the
	 *       maximum number of headlines to fetch (1 - 300)
	 *       historicalNewsOptions reserved for internal use. Should be defined
	 *       as null.
	 *
	 * @see EWrapper::historicalNews, EWrapper::historicalNewsEnd
	 */
	void reqHistoricalNews(int requestId, int conId, String providerCodes, String startDateTime, String endDateTime,
			int totalResults, List<TagValue> historicalNewsOptions) {
		clientSocket.reqHistoricalNews(requestId, conId, providerCodes, startDateTime, endDateTime, totalResults,
				historicalNewsOptions);
	}

	/**
	 * Requests historical Time&Sales data for an instrument.
	 * 
	 * @param reqId
	 *            id of the request contract Contract object that is subject of
	 *            query startDateTime,i.e. "20170701 12:01:00". Uses TWS
	 *            timezone specified at login. endDateTime,i.e.
	 *            "20170701 13:01:00". In TWS timezone. Exactly one of start
	 *            time and end time has to be defined. numberOfTicks Number of
	 *            distinct data points. Max currently 1000 per request.
	 *            whatToShow (Bid_Ask, Midpoint, Trades) Type of data requested.
	 *            useRth Data from regular trading hours (1), or all available
	 *            hours (0) ignoreSize A filter only used when the source price
	 *            is Bid_Ask miscOptions should be defined as null, reserved for
	 *            internal use
	 */
	void reqHistoricalTicks(int reqId, Contract contract, String startDateTime, String endDateTime, int numberOfTicks,
			String whatToShow, int useRth, Boolean ignoreSize, List<TagValue> miscOptions) {
		clientSocket.reqHistoricalTicks(reqId, contract, startDateTime, endDateTime, numberOfTicks, whatToShow, useRth,
				ignoreSize, miscOptions);
	}

	/**
	 * Requests the next valid order ID at the current moment.
	 * 
	 * @param numIds
	 *            deprecated- this parameter will not affect the value returned
	 *            to nextValidId
	 * 
	 * @see EWrapper::nextValidId
	 */
	void reqIds(int numIds) {
		clientSocket.reqIds(numIds);
	}

	/**
	 * Requests the accounts to which the logged user has access to.
	 * 
	 * @see EWrapper::managedAccounts
	 */
	void reqManagedAccts() {
		clientSocket.reqManagedAccts();
	}

	/**
	 * Requests the contract's market depth (order book). This request must be
	 * direct-routed to an exchange and not smart-routed. The number of
	 * simultaneous market depth requests allowed in an account is calculated
	 * based on a formula that looks at an accounts equity, commissions, and
	 * quote booster packs.
	 * 
	 * @param tickerId
	 *            the request's identifier contract the Contract for which the
	 *            depth is being requested numRows the number of rows on each
	 *            side of the order book
	 * 
	 * @see cancelMktDepth, EWrapper::updateMktDepth, EWrapper::updateMktDepthL2
	 */
	void reqMktDepth(int tickerId, Contract contract, int numRows, List<TagValue> mktDepthOptions) {
		clientSocket.reqMktDepth(tickerId, contract, numRows, mktDepthOptions);
	}

	/**
	 * Requests details about a given market rule
	 * 
	 * The market rule for an instrument on a particular exchange provides
	 * details about how the minimum price increment changes with price A list
	 * of market rule ids can be obtained by invoking reqContractDetails on a
	 * particular contract. The returned market rule ID list will provide the
	 * market rule ID for the instrument in the correspond valid exchange list
	 * in contractDetails.
	 * 
	 * @param marketRuleId
	 *            - the id of market rule
	 * 
	 * @see EWrapper::marketRule
	 */
	void reqMarketRule(int marketRuleId) {
		clientSocket.reqMarketDataType(marketRuleId);
	}

	/**
	 * Requests matching stock symbols.
	 * 
	 * @param reqId
	 *            id to specify the request pattern - either start of ticker
	 *            symbol or (for larger strings) company name
	 * 
	 * @see EWrapper::symbolSamples
	 */
	void reqMatchingSymbols(int reqId, String pattern) {
		clientSocket.reqMatchingSymbols(reqId, pattern);
	}

	/**
	 * Requests real time market data. Returns market data for an instrument
	 * either in real time or 10-15 minutes delayed (depending on the market
	 * data type specified)
	 * 
	 * @param tickerId
	 *            the request's identifier contract the Contract for which the
	 *            data is being requested genericTickList comma separated ids of
	 *            the available generic ticks: 100 Option Volume (currently for
	 *            stocks) 101 Option Open Interest (currently for stocks) 104
	 *            Historical Volatility (currently for stocks) 105 Average
	 *            Option Volume (currently for stocks) 106 Option Implied
	 *            Volatility (currently for stocks) 162 Index Future Premium 165
	 *            Miscellaneous Stats 221 Mark Price (used in TWS P&L
	 *            computations) 225 Auction values (volume, price and imbalance)
	 *            233 RTVolume - contains the last trade price, last trade size,
	 *            last trade time, total volume, VWAP, and single trade flag.
	 *            236 Shortable 256 Inventory 258 Fundamental Ratios 411
	 *            Realtime Historical Volatility 456 IBDividends snapshot for
	 *            users with corresponding real time market data subscriptions.
	 *            A true value will return a one-time snapshot, while a false
	 *            value will provide streaming data. regulatory snapshot for US
	 *            stocks requests NBBO snapshots for users which have
	 *            "US Securities Snapshot Bundle" subscription but not
	 *            corresponding Network A, B, or C subscription necessary for
	 *            streaming * market data. One-time snapshot of current market
	 *            price that will incur a fee of 1 cent to the account per
	 *            snapshot.
	 * 
	 * @see cancelMktData, EWrapper::tickPrice, EWrapper::tickSize,
	 *      EWrapper::tickString, EWrapper::tickEFP, EWrapper::tickGeneric,
	 *      EWrapper::tickOptionComputation, EWrapper::tickSnapshotEnd
	 */
	void reqMktData(int tickerId, Contract contract, String genericTickList, Boolean snapshot,
			Boolean regulatorySnapshot, List<TagValue> mktDataOptions) {
		clientSocket.reqMktData(tickerId, contract, genericTickList, snapshot, regulatorySnapshot, mktDataOptions);
	}

	/**
	 * @return the timestamp of earliest available historical data for a
	 *         contract and data type.
	 * 
	 * @param tickerId
	 *            - an identifier for the request contract - contract object for
	 *            which head timestamp is being requested whatToShow - type of
	 *            data for head timestamp - "BID", "ASK", "TRADES", etc useRTH -
	 *            use regular trading hours only, 1 for yes or 0 for no
	 *            formatDate set to 1 to obtain the bars' time as yyyyMMdd
	 *            HH:mm:ss, set to 2 to obtain it like system time format in
	 *            seconds
	 *
	 * @See headTimeStamp
	 */
	void reqHeadTimestamp(int tickerId, Contract contract, String whatToShow, int useRTH, int formatDate) {
		clientSocket.reqHeadTimestamp(tickerId, contract, whatToShow, useRTH, formatDate);
	}

	public void placeOrder(Contract contract, Order order) {
		clientSocket.placeOrder(ibClient.getNextOrderId(), contract, order);
	}

}