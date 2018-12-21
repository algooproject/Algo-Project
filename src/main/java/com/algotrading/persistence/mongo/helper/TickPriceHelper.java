package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.TickPrice;
import com.mongodb.DB;

public class TickPriceHelper extends DBObjectableHelper<TickPrice> {

	public TickPriceHelper(DB db, MongoDBLogger logger) {
		super(db, logger, new TickPrice());
	}

}
