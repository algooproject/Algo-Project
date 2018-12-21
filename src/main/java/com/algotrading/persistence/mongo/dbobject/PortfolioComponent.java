package com.algotrading.persistence.mongo.dbobject;

public class PortfolioComponent {
	// Contract contract, double position, double marketPrice, double
	// marketValue,
	// double averageCost, double unrealizedPNL, double realizedPNL, String
	// accountName

	public static final String FIELD_POSITION = "position";
	public static final String FIELD_MARKET_PRICE = "marketPrice";
	public static final String FIELD_MARKET_VALUE = "marketValue";
	public static final String FIELD_AVERAGE_COST = "averageCost";
	public static final String FIELD_UNREALIZED_PNL = "unrealizedPNL";
	public static final String FIELD_REALIZED_PNL = "realizedPNL";

	private double position;
	private double marketPrice;
	private double marketValue;
	private double averageCost;
	private double unrealizedPNL;
	private double realizedPNL;

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public double getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(double marketValue) {
		this.marketValue = marketValue;
	}

	public double getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(double averageCost) {
		this.averageCost = averageCost;
	}

	public double getUnrealizedPNL() {
		return unrealizedPNL;
	}

	public void setUnrealizedPNL(double unrealizedPNL) {
		this.unrealizedPNL = unrealizedPNL;
	}

	public double getRealizedPNL() {
		return realizedPNL;
	}

	public void setRealizedPNL(double realizedPNL) {
		this.realizedPNL = realizedPNL;
	}

	@Override
	public String toString() {
		return "PortfolioComponent [position=" + position + ", marketPrice=" + marketPrice + ", marketValue="
				+ marketValue + ", averageCost=" + averageCost + ", unrealizedPNL=" + unrealizedPNL + ", realizedPNL="
				+ realizedPNL + "]";
	}

}
