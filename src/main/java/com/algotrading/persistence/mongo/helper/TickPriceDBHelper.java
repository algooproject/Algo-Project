package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.TickPrice;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class TickPriceDBHelper {
	private final MongoDBLogger logger;
	private final DBCollection databaseExamplescollectionTickPrice;

	public TickPriceDBHelper(DB databaseExamples, MongoDBLogger logger) {
		this.logger = logger;
		this.databaseExamplescollectionTickPrice = databaseExamples.getCollection("tickPrice");
	}

	public void insert(TickPrice tickPrice) {
		logger.logInsert(tickPrice);
		databaseExamplescollectionTickPrice.insert(tickPrice.toDBObject());
	}
}
