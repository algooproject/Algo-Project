package com.algotrading.backtesting.strategy;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
		// System.out.println("shouldPutOrder: " + date);
		return pattern.signal(stock, date, portfolio, buyCostIfMatch);
	}

	public PortfolioComponent buyAmount(Stock stock, Date date, Portfolio portfolio) {
		double unitPrice = stock.getHistory().get(date).getClose();
		double buyCost = buyCostIfMatch == 0 ? portfolio.getCash() : buyCostIfMatch;
		// int buyVolume = getFinalVolume(stock, buyCost, unitPrice);
		Pair<Integer, Double> volumeTransactionCost = getFinalVolume(stock, buyCost, unitPrice);
		// System.out.println("stock.getTicker(): " + stock.getTicker());
		// System.out.println("stock.getLotSize(): " + stock.getLotSize());
		// System.out
		// .println("buyVolumeBeforeLotSize / stock.getLotSize(): " +
		return new PortfolioComponent(stock, volumeTransactionCost.getKey(), unitPrice, date,
				volumeTransactionCost.getValue(), "Open");
	}

	private Pair<Integer, Double> getFinalVolume(Stock stock, double buyCost, double unitPrice) {
		int buyVolumeBeforeLotSize = (int) (buyCost / unitPrice);
		int buyVolumeAfterLotSize = (buyVolumeBeforeLotSize / stock.getLotSize()) * stock.getLotSize();
		double buyTotal = buyVolumeAfterLotSize * unitPrice;
		double transactionCost = Transaction.getTranscationCost(stock, buyTotal);
		while (transactionCost + buyTotal > buyCost) {
			buyVolumeAfterLotSize = buyVolumeAfterLotSize - stock.getLotSize();
			buyTotal = buyVolumeAfterLotSize * unitPrice;
			transactionCost = Transaction.getTranscationCost(stock, buyTotal);
		}
		return new MutablePair<Integer, Double>(buyVolumeAfterLotSize, transactionCost);
	}

	public PortfolioComponent sellAmount(Stock stock, Date date, Portfolio portfolio) {
		double unitPrice = stock.getHistory().get(date).getClose();
		PortfolioComponent pc = portfolio.getPortfolioComponent(stock.getTicker());
		int sellVolumeBeforeLotSize = Math.max((int) (buyCostIfMatch / unitPrice), pc.getQuantity());
		// System.out.println("stock.getTicker(): " + stock.getTicker());
		// System.out.println("stock.getLotSize(): " + stock.getLotSize());
		String action = unitPrice > pc.getUnitPrice() ? "Profit" : "Exit";
		int sellVolumeAfterLotSize = (sellVolumeBeforeLotSize / stock.getLotSize()) * stock.getLotSize();
		int sellVolumeAfterPossibleSoldAll = buyCostIfMatch == 0
				? portfolio.getPortfolioComponent(stock.getTicker()).getQuantity() : sellVolumeAfterLotSize;
		return new PortfolioComponent(stock, 0 - sellVolumeAfterPossibleSoldAll, unitPrice, date,
				Transaction.getTranscationCost(stock, sellVolumeBeforeLotSize * unitPrice), action);
	}
}
