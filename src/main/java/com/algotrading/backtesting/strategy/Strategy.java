package com.algotrading.backtesting.strategy;

import java.text.ParseException;
import java.util.Date;

import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.portfolio.PortfolioComponent;
import com.algotrading.backtesting.replay.Transaction;
import com.algotrading.backtesting.stock.Stock;

public class Strategy {
	private StockSignal pattern;
	private double buyCostIfMatch;

	public Strategy(StockSignal pattern, double buyCostIfMatch) {
		super();
		this.pattern = pattern;
		this.buyCostIfMatch = buyCostIfMatch;
	}

	public boolean shouldPutOrder(Stock stock, Date date, Portfolio portfolio) throws ParseException {
//		System.out.println("shouldPutOrder: " + date);
		return pattern.signal(stock, date, portfolio, buyCostIfMatch);
	}

	public PortfolioComponent buyAmount(Stock stock, Date date, Portfolio portfolio) {
		double unitPrice = stock.getHistory().get(date).getClose();
		double buyCost = buyCostIfMatch == 0 ? portfolio.getCash() : buyCostIfMatch;
		int buyVolume = getFinalVolume(stock, buyCost, unitPrice);
		// System.out.println("stock.getTicker(): " + stock.getTicker());
		// System.out.println("stock.getLotSize(): " + stock.getLotSize());
		// System.out
		// .println("buyVolumeBeforeLotSize / stock.getLotSize(): " +
		// buyVolumeBeforeLotSize / stock.getLotSize());
		// System.out.println("buyVolumeBeforeLotSize: " +
		// buyVolumeBeforeLotSize);
		// System.out.println("buyBolumeAfterLotSize: " +
		// buyBolumeAfterLotSize);
		return new PortfolioComponent(stock, buyVolume, unitPrice, date);
	}
	
	private int getFinalVolume(Stock stock, double buyCost, double unitPrice){
		int buyVolumeBeforeLotSize = (int) (buyCost / unitPrice);
		int buyVolumeAfterLotSize = (buyVolumeBeforeLotSize / stock.getLotSize()) * stock.getLotSize();
		double buyTotal = buyVolumeAfterLotSize * unitPrice;
		while ( Transaction.getTranscationCost(stock, buyTotal) +  buyTotal > buyCost){
			buyVolumeAfterLotSize = buyVolumeAfterLotSize - stock.getLotSize();
			buyTotal = buyVolumeAfterLotSize * unitPrice;
		}
		return buyVolumeAfterLotSize;
	}

	public PortfolioComponent sellAmount(Stock stock, Date date, Portfolio portfolio) {
		double unitPrice = stock.getHistory().get(date).getClose();
		int sellVolumeBeforeLotSize = Math.max((int) (buyCostIfMatch / unitPrice),
				portfolio.getPortfolioComponent(stock.getTicker()).getQuantity());
		// System.out.println("stock.getTicker(): " + stock.getTicker());
		// System.out.println("stock.getLotSize(): " + stock.getLotSize());
		int sellVolumeAfterLotSize = (sellVolumeBeforeLotSize / stock.getLotSize()) * stock.getLotSize();
		int sellVolumeAfterPossibleSoldAll = buyCostIfMatch == 0
				? portfolio.getPortfolioComponent(stock.getTicker()).getQuantity() : sellVolumeAfterLotSize;
		return new PortfolioComponent(stock, 0 - sellVolumeAfterPossibleSoldAll, unitPrice, date);
	}
}
