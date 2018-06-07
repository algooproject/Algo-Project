package com.algotrading.backtesting.strategy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.algotrading.backtesting.pattern.StockSignal;
import com.algotrading.backtesting.patterninterperter.Interperter;
import com.algotrading.backtesting.portfolio.BuySellAmount;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.portfolio.PortfolioComponent;
import com.algotrading.backtesting.stock.Stock;

public class Strategies {
	private List<Strategy> buySignal;
	private List<Strategy> sellSignal;
	private List<Strategy> exitSignal;
	private List<Strategy> reentrySignal;

	public Strategies() {
		buySignal = new ArrayList<>();
		sellSignal = new ArrayList<>();
		exitSignal = new ArrayList<>();
		reentrySignal = new ArrayList<>();
	}

	public Strategies(String buyStrategiesFilePath, String sellStrategiesFilePath) throws IOException, ParseException {
		buySignal = read(buyStrategiesFilePath);
		sellSignal = read(sellStrategiesFilePath);
		exitSignal = new ArrayList<>();
		reentrySignal = new ArrayList<>();
	}

	public Strategies(String buyStrategiesFilePath, String sellStrategiesFilePath, String exitStrategiesFilePath,
			String reentryStrategiesFilePath) throws IOException, ParseException {
		buySignal = read(buyStrategiesFilePath);
		sellSignal = read(sellStrategiesFilePath);
		exitSignal = read(exitStrategiesFilePath);
		reentrySignal = read(reentryStrategiesFilePath);
	}

	public void addBuySignal(Strategy strategy) {
		buySignal.add(strategy);
	}

	public void addSellSignal(Strategy strategy) {
		sellSignal.add(strategy);
	}

	public List<Strategy> read(String strategiesFilePath) throws IOException, ParseException {
		Path filePath = new File(strategiesFilePath).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		List<Strategy> strategies = new ArrayList<>();
		for (String line : stringList) {
			String[] value = line.split("\t");
			String signalStr = value[0];
			String costStr = value[1];
			StockSignal stockPattern = Interperter.toPattern(signalStr);
			double cost = Double.parseDouble(costStr);
			Strategy strategy = new Strategy(stockPattern, cost);
			strategies.add(strategy);
		}
		return strategies;
	}

	public BuySellAmount buySellAmount(Stock stock, Date date, Portfolio portfolio) throws ParseException {
		if (!stock.getStatus()) {
			for (Strategy strategy : reentrySignal) {
				if (strategy.shouldPutOrder(stock, date,
						portfolio)/* && !portfolio.containsStock(stock) */) {
					stock.setStatus(true);
					break;
				}
			}
		}
		if (stock.getStatus()) {
			for (Strategy strategy : buySignal) {
				if (strategy.shouldPutOrder(stock, date,
						portfolio)/* && !portfolio.containsStock(stock) */) {
					PortfolioComponent buyAmount = strategy.buyAmount(stock, date, portfolio);
					// sellAmount.getQuantity() should be positive as portfolio
					// increase stock
					// so need to negative it to state decrease cash
					double transactionCost = buyAmount.getTransactionCost();
					double tradedCash = -buyAmount.getUnitPrice() * buyAmount.getQuantity() - transactionCost;
					return new BuySellAmount(buyAmount, tradedCash, transactionCost);
				}
			}
		}

		for (Strategy strategy : exitSignal) {
			if (strategy.shouldPutOrder(stock, date, portfolio) && portfolio.containsStock(stock)) {
				PortfolioComponent sellAmount = strategy.sellAmount(stock, date, portfolio);
				// sellAmount.getQuantity() should be negative as portfolio
				// decrease stock
				// so need to negative it to state increase cash
				stock.setStatus(false);
				double transactionCost = sellAmount.getTransactionCost();
				double tradedCash = -sellAmount.getUnitPrice() * sellAmount.getQuantity() - transactionCost;
				return new BuySellAmount(sellAmount, tradedCash, transactionCost);
			}
		}

		for (Strategy strategy : sellSignal) {
			if (strategy.shouldPutOrder(stock, date, portfolio) && portfolio.containsStock(stock)) {
				PortfolioComponent sellAmount = strategy.sellAmount(stock, date, portfolio);

				// sellAmount.getQuantity() should be negative as portfolio
				// decrease stock
				// so need to negative it to state increase cash
				double transactionCost = sellAmount.getTransactionCost();
				double tradedCash = -sellAmount.getUnitPrice() * sellAmount.getQuantity() - transactionCost;
				return new BuySellAmount(sellAmount, tradedCash, transactionCost);
			}
		}
		return new BuySellAmount(new PortfolioComponent(stock, 0, 0, date, 0), 0);
	}
}
