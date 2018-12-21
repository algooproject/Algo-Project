package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.Portfolio;
import com.mongodb.DB;

public class PortfolioHelper extends DBObjectableHelper<Portfolio> {

	public PortfolioHelper(DB db, MongoDBLogger logger) {
		super(db, logger, new Portfolio());
	}
}
