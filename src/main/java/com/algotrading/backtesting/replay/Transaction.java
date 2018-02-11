package com.algotrading.backtesting.replay;

import com.algotrading.backtesting.stock.Stock;

public class Transaction {

	// can be a place to store all the transactions by date
	
	private static double testCommissionRate = 0;
	
	public static double getTranscationCost(Stock stock, double volume){
		switch( getTransactionModelString( stock.getTicker() ) ){
		case "HK_Stock_General":
			return getHK_Stock_General_TransactionCost(stock, volume);
        default: 
        	return Math.abs(volume*testCommissionRate); // default 3% of the total volume
		}
		// TODO: more transaction models are required later
	}
	
	private static String getTransactionModelString(String ticker){
		String indicator = ticker.substring(ticker.lastIndexOf( '.' ) + 1);
		// System.out.println("transaction String: " + indicator);
		if ( indicator.equals("HK")){
			// System.out.println("HK_Stock_General");
			return "HK_Stock_General";
		}
		return "Nothing";
	}
	
	private static double getHK_Stock_General_TransactionCost(Stock stock, double volume){
		return Math.abs(volume*0.01);
	}
}
