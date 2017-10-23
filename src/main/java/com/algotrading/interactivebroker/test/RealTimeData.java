package com.algotrading.interactivebroker.test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set; // Needed for IB API 9.72.14

//RealTimeData.java    
//API Version 9.72 and newer
//Version 1.3
//20170506
//R. Holowczak

//Import Java utilities and Interactive Brokers API
// http://interactivebrokers.github.io/#
// https://www.interactivebrokers.com/en/index.php?f=5041
import java.util.Vector;

import org.springframework.core.annotation.Order;

import com.ib.client.Bar;
import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDescription;
import com.ib.client.ContractDetails;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.DepthMktDataDescription;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.FamilyCode;
import com.ib.client.HistogramEntry;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.NewsProvider;
import com.ib.client.OrderState;
import com.ib.client.PriceIncrement;
import com.ib.client.SoftDollarTier;
import com.ib.client.TagValue;
import com.ib.client.TickAttr;

//RealTimeData Class is an implementation of the 
//IB API EWrapper class
public class RealTimeData implements EWrapper {
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

	// New for API version 9.72.14
	@Override
	public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId,
			String tradingClass, String multiplier, Set expirations, Set strikes) {
		// TODO Auto-generated method stub
	}

	// New for API version 9.72.14
	@Override
	public void securityDefinitionOptionalParameterEnd(int reqId) {
		// TODO Auto-generated method stub
	}

	// New for API version 9.72.14
	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value,
			String currency) {
		// TODO Auto-generated method stub
	}

	// New for API version 9.72.14
	@Override
	public void accountUpdateMultiEnd(int reqId) {
		// TODO Auto-generated method stub
	}

	// New for API version 9.72.14
	@Override
	public void positionMulti(int reqId, String account, String modelCode, Contract contract, double pos,
			double avgCost) {
		// TODO Auto-generated method stub
	}

	// New for API version 9.72.14
	@Override
	public void positionMultiEnd(int reqId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
	}

	@Override
	public void contractDetailsEnd(int reqId) {
	}

	@Override
	public void fundamentalData(int reqId, String data) {
	}

	public void bondContractDetails(ContractDetails contractDetails) {
	}

	public void contractDetails(ContractDetails contractDetails) {
	}

	@Override
	public void currentTime(long time) {
	}

	@Override
	public void displayGroupList(int requestId, String contraftInfo) {
	}

	@Override
	public void displayGroupUpdated(int requestId, String contractInfo) {
	}

	// Add for API version 9.72
	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
	}

	// Add for API version 9.72
	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
	}

	@Override
	public void verifyCompleted(boolean completed, String contractInfo) {
	}

	@Override
	public void verifyMessageAPI(String message) {
	}

	@Override
	public void execDetails(int orderId, Contract contract, Execution execution) {
	}

	@Override
	public void execDetailsEnd(int reqId) {
	}

	public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume,
			int count, double WAP, boolean hasGaps) {
	}

	@Override
	public void managedAccounts(String accountsList) {
	}

	@Override
	public void commissionReport(CommissionReport cr) {
	}

	// For API Version 9.72 pos is now a double
	@Override
	public void position(String account, Contract contract, double pos, double avgCost) {
	}

	// Below is API version 9.71
	public void position(String account, Contract contract, int pos, double avgCost) {
	}

	@Override
	public void positionEnd() {
	}

	@Override
	public void accountSummary(int reqId, String account, String tag, String value, String currency) {
	}

	@Override
	public void accountSummaryEnd(int reqId) {
	}

	@Override
	public void accountDownloadEnd(String accountName) {
	}

	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
	}

	@Override
	public void openOrderEnd() {
	}

	// For API Version 9.72
	public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
	}

	// For API Version 9.71
	public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId,
			int parentId, double lastFillPrice, int clientId, String whyHeld) {
	}

	@Override
	public void receiveFA(int faDataType, String xml) {
	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
	}

	@Override
	public void scannerDataEnd(int reqId) {
	}

	@Override
	public void scannerParameters(String xml) {
	}

	@Override
	public void tickEFP(int symbolId, int tickType, double basisPoints, String formattedBasisPoints,
			double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {
	}

	@Override
	public void tickGeneric(int symbolId, int tickType, double value) {
	}

	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
	}

	// public void deltaNeutralValidation(int reqId, UnderComp underComp)
	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
	}

	@Override
	public void updateAccountTime(String timeStamp) {
	}

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) {
	}

	@Override
	public void updateMktDepth(int symbolId, int position, int operation, int side, double price, int size) {
	}

	@Override
	public void updateMktDepthL2(int symbolId, int position, String marketMaker, int operation, int side, double price,
			int size) {
	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
	}

	// For API Version 9.72
	@Override
	public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
	}

	// For API Version 9.71:
	public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue,
			double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
	}

	@Override
	public void tickSnapshotEnd(int tickerId) {
	}

	@Override
	public void connectionClosed() {
	}

	// Add connectAck for API version 9.72
	@Override
	public void connectAck() {
	}

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume,
			double wap, int count) {
	}

	@Override
	public void error(Exception e) {
		// Print out a stack trace for the exception
		e.printStackTrace();
	}

	@Override
	public void error(String str) {
		// Print out the error message
		System.err.println(str);
	}

	@Override
	public void error(int id, int errorCode, String errorMsg) {
		// Overloaded error event (from IB) with their own error
		// codes and messages
		System.err.println("error: " + id + "," + errorCode + "," + errorMsg);
	}

	@Override
	public void nextValidId(int orderId) {
		// Return the next valid OrderID
		nextOrderID = orderId;
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
		// field will provide the size type:
		// 0=bid size, 3=ask size, 5=last size, 8=volume
		// System.out.println("tickSize: " + orderId + "," + field + "," +
		// size);
		System.out.println("tickSize: " + orderId + "," + field + "," + size);
	}

	@Override
	public void tickString(int orderId, int tickType, String value) {
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

	@Override
	public void familyCodes(FamilyCode[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void headTimestamp(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void histogramData(int arg0, List<HistogramEntry> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalData(int arg0, Bar arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalDataEnd(int arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalDataUpdate(int arg0, Bar arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalNews(int arg0, String arg1, String arg2, String arg3, String arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalNewsEnd(int arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalTicks(int arg0, List<HistoricalTick> arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalTicksBidAsk(int arg0, List<HistoricalTickBidAsk> arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void historicalTicksLast(int arg0, List<HistoricalTickLast> arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void marketRule(int arg0, PriceIncrement[] arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mktDepthExchanges(DepthMktDataDescription[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newsArticle(int arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newsProviders(NewsProvider[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openOrder(int arg0, Contract arg1, com.ib.client.Order arg2, OrderState arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void orderStatus(int arg0, String arg1, double arg2, double arg3, double arg4, int arg5, int arg6,
			double arg7, int arg8, String arg9, double arg10) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pnl(int arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pnlSingle(int arg0, int arg1, double arg2, double arg3, double arg4, double arg5) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rerouteMktDataReq(int arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rerouteMktDepthReq(int arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void smartComponents(int arg0, Map<Integer, Entry<String, Character>> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void softDollarTiers(int arg0, SoftDollarTier[] arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void symbolSamples(int arg0, ContractDescription[] arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tickNews(int arg0, long arg1, String arg2, String arg3, String arg4, String arg5) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tickPrice(int arg0, int arg1, double arg2, TickAttr arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tickReqParams(int arg0, double arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub

	}

} // end public class RealTimeData