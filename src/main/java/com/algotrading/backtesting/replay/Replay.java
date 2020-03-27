package com.algotrading.backtesting.replay;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.algotrading.backtesting.portfolio.BuySellAmount;
import com.algotrading.backtesting.portfolio.Portfolio;
import com.algotrading.backtesting.portfolio.PortfolioComponent;
import com.algotrading.backtesting.stock.PortfolioHistory;
import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.strategy.Strategies;
import com.algotrading.backtesting.util.PrintMethod;

public class Replay {
	private Date startDate;
	private Date endDate;
	private PortfolioHistory portfolioHistory;
	public Strategies strategies;
	// private AvailableStocks availableStocks;
	private AvailableStocksWithYearChange availableStocksWithYearChange;
	private TradingDate tradingDate;
	private Boolean recordSwitch = true;

	private Portfolio portfolio;
	private double totalTradedVolume = 0;
	private double totalTrasactionCost = 0;

	private PrintMethod print;

	public Long buySellAmountTime = 0l;

	public void setRecordSwitch(Boolean flag) {
		recordSwitch = flag;
	}

	public double getTotalTradedVolume() {
		return totalTradedVolume;
	}

	public double getTotalTransactionCost() {
		return totalTrasactionCost;
	}

	private void setData(Date startDate, Date endDate, PortfolioHistory portfolioHistory, Strategies strategies,
			AvailableStocksWithYearChange availableStocksWithYearChange, TradingDate tradingDate, double initialCash) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.portfolioHistory = portfolioHistory;
		this.strategies = strategies;
		this.availableStocksWithYearChange = availableStocksWithYearChange;
		this.tradingDate = tradingDate;

		this.portfolio = portfolioHistory.get(startDate);
		if (this.portfolio != null) {
			this.portfolio.addCash(initialCash);

		} else {
			this.portfolio = new Portfolio(startDate, initialCash);
			this.portfolioHistory.put(startDate, portfolio);
		}
		this.portfolioHistory.setInitValue(this.portfolio.marketValue());
		portfolioHistoryInit();
	}

	public Replay(Date startDate, Date endDate, PortfolioHistory portfolioHistory, Strategies strategies,
			AvailableStocksWithYearChange availableStocksWithYearChange, TradingDate tradingDate, double initialCash,
			PrintMethod printMethod) throws ParseException {
		print = printMethod;
		print.setDatesAndHistory(startDate, endDate, portfolioHistory);
		setData(startDate, endDate, portfolioHistory, strategies, availableStocksWithYearChange, tradingDate,
				initialCash);
	}

	public void simulate() throws ParseException {
		LocalTime startTime, endTime;
		// System.out.println("Simulation started... ");
		Date currentDate = startDate;
		// System.out.println();
		// System.out.println(startDate);
		tradingDate.setCurrentDate(currentDate);
		while (tradingDate.isNotLastDate() && tradingDate.currentDate()
				.compareTo(endDate) <= 0) {
			currentDate = tradingDate.currentDate();
			portfolio.setDate(currentDate);
			AvailableStocks availableStocks = availableStocksWithYearChange.get(currentDate);
			Map<String, Stock> allAvailableStocks = availableStocksWithYearChange.getAllAvailableStocks(); // stocks
																											// for
																											// all
																											// years

			Set<Stock> availableStocksWithPortfolio = new LinkedHashSet<>();
			availableStocksWithPortfolio.addAll(availableStocks.get());
			availableStocksWithPortfolio.addAll(portfolio.getAllTickerName()
					.stream()
					.map(ticker -> allAvailableStocks.get(ticker))
					.collect(Collectors.toSet()));

			for (Stock stock : availableStocksWithPortfolio) {
				// System.out.println("simulate: " + currentDate);
				startTime = LocalTime.now();
				BuySellAmount buySellAmount = strategies.buySellAmount(stock, currentDate, portfolio);
				endTime = LocalTime.now();
				buySellAmountTime = buySellAmountTime + startTime.until(endTime, MILLIS);
				PortfolioComponent component = buySellAmount.getPortfolioComponent();
				if (component.getQuantity() != 0) {
					totalTradedVolume = totalTradedVolume
							+ Math.abs(component.getQuantity() * component.getUnitPrice());
					totalTrasactionCost = totalTrasactionCost + buySellAmount.getTransaction();
					double tradedCash = buySellAmount.getTradedCash();

					portfolio.add(component);
					portfolio.addCash(tradedCash);
					portfolio.addTransaction(buySellAmount);
				}
			}
			if (recordSwitch) {
				portfolioHistory.put(currentDate, portfolio);
				portfolioHistory.addTransactions(portfolio.getTransactions());
				List<BuySellAmount> currentTransactions = portfolio.getTransactions();
				Map<String, PortfolioComponent> ticker_pc = portfolio.getTicker_pc();
				for (int i = 0; i < currentTransactions.size(); i++) {
					PortfolioComponent pc = currentTransactions.get(i)
							.getPortfolioComponent()
							.clone();
					String ticker = pc.getStock()
							.getTicker();
					if (ticker_pc.containsKey(ticker)) {
						double buyPrice = ticker_pc.get(ticker)
								.getUnitPrice();
						double sellPrice = pc.getUnitPrice();
						if (buyPrice < sellPrice) {
							currentTransactions.get(i)
									.setAction("TakeProfit");

						} else {
							currentTransactions.get(i)
									.setAction("StopLoss");
						}
						ticker_pc.get(ticker)
								.add(pc);
						if (ticker_pc.get(ticker)
								.getQuantity() == 0)
							ticker_pc.remove(ticker);

					} else {
						currentTransactions.get(i)
								.setAction("Open");
						ticker_pc.put(ticker, pc);
					}
				}
				portfolio.setTicker_pc(ticker_pc);
				print.record(currentDate, portfolio);
			}
			portfolio = portfolio.clone();
			tradingDate.rollDay();
		}
	}

	public PortfolioHistory getPortfolioHistory() {
		return portfolioHistory;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void print() throws ParseException {
		print.print();
	}

	public PrintMethod getPrintMethod() {
		return print;
	}

	private void portfolioHistoryInit() {
		Map<String, PortfolioComponent> portfolioComponents = portfolioHistory.get(startDate)
				.getPortfolioComponents();
		for (String key : portfolioComponents.keySet()) {
			if (portfolioComponents.get(key)
					.getQuantity() != 0) {
				PortfolioComponent pc = portfolioComponents.get(key)
						.clone();
				portfolio.getTicker_pc()
						.put(key, pc);
			}
		}
	}

}
