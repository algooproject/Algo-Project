package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.StockInfo;
import com.mongodb.DB;

public class StockInfoHelper extends DBObjectableHelper<StockInfo> {

	public StockInfoHelper(DB db, MongoDBLogger logger) {
		super(db, logger, new StockInfo());
	}

}
