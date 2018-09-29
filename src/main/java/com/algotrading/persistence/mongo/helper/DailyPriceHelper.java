package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.DailyPrice;
import com.mongodb.DB;

public class DailyPriceHelper extends DBObjectableHelper<DailyPrice> {

	public DailyPriceHelper(DB db, MongoDBLogger logger) {
		super(db, logger, new DailyPrice());
	}

}
