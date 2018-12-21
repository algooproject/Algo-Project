package com.algotrading.persistence.mongo.dbobject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Portfolio implements DBObjectable {

	public static final String FIELD_ACCOUNT_NAME = "accountName";
	public static final String FIELD_PORTFOLIO = "portfolio";

	private String accountName;
	private Map<String, PortfolioComponent> portfolios = new HashMap<>();

	@Override
	public DBObject toDBObject() {
		BasicDBObject dbObject = new BasicDBObject("_id", getKey()).append(FIELD_ACCOUNT_NAME, getAccountName());
		BasicDBObject portfolioDBObject = new BasicDBObject();
		for (Map.Entry<String, PortfolioComponent> entry : portfolios.entrySet()) {
			BasicDBObject object = new BasicDBObject().append(PortfolioComponent.FIELD_AVERAGE_COST, entry.getValue()
					.getAverageCost())
					.append(PortfolioComponent.FIELD_MARKET_PRICE, entry.getValue()
							.getMarketPrice())
					.append(PortfolioComponent.FIELD_MARKET_VALUE, entry.getValue()
							.getMarketValue())
					.append(PortfolioComponent.FIELD_POSITION, entry.getValue()
							.getPosition())
					.append(PortfolioComponent.FIELD_REALIZED_PNL, entry.getValue()
							.getRealizedPNL())
					.append(PortfolioComponent.FIELD_UNREALIZED_PNL, entry.getValue()
							.getUnrealizedPNL());
			portfolioDBObject.append(entry.getKey(), object);
		}
		dbObject.append(FIELD_PORTFOLIO, portfolioDBObject);
		return dbObject;
	}

	@Override
	public void fromDBObject(DBObject dbObject) {
		accountName = (String) dbObject.get(FIELD_ACCOUNT_NAME);
		BasicDBObject portfolioComponentDBObject = (BasicDBObject) dbObject.get(FIELD_PORTFOLIO);

		for (String key : portfolioComponentDBObject.keySet()) {
			BasicDBObject portfolioDBObject = (BasicDBObject) portfolioComponentDBObject.get(key);
			PortfolioComponent value = new PortfolioComponent();
			value.setAverageCost(portfolioDBObject.getDouble(PortfolioComponent.FIELD_AVERAGE_COST));
			value.setMarketPrice(portfolioDBObject.getDouble(PortfolioComponent.FIELD_MARKET_PRICE));
			value.setMarketValue(portfolioDBObject.getDouble(PortfolioComponent.FIELD_MARKET_VALUE));
			value.setPosition(portfolioDBObject.getDouble(PortfolioComponent.FIELD_POSITION));
			value.setRealizedPNL(portfolioDBObject.getDouble(PortfolioComponent.FIELD_REALIZED_PNL));
			value.setUnrealizedPNL(portfolioDBObject.getDouble(PortfolioComponent.FIELD_UNREALIZED_PNL));
			portfolios.put(key, value);
		}
	}

	@Override
	public String getCollectionName() {
		return "portfolio";
	}

	@Override
	public String getKey() {
		return accountName;
	}

	@Override
	public Supplier<? extends DBObjectable> getSupplier() {
		return Portfolio::new;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Map<String, PortfolioComponent> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(Map<String, PortfolioComponent> portfolios) {
		this.portfolios = portfolios;
	}

	@Override
	public String toString() {
		return "Portfolio [accountName=" + accountName + ", portfolios=" + portfolios + "]";
	}

}
