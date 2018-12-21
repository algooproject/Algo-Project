package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.DummyTickPrice;
import com.mongodb.DB;

public class DummyTickPriceDBHelper extends DBObjectableHelper<DummyTickPrice> {

	public DummyTickPriceDBHelper(DB databaseExamples, MongoDBLogger logger) {
		super(databaseExamples, logger, new DummyTickPrice());
	}

}
