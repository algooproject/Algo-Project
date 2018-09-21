package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.DailyPrice;
import com.algotrading.persistence.mongo.dbobject.DummyTickPrice;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DailyPriceHelper {
	private final MongoDBLogger logger;
	private final DBCollection dbDailyPriceCollection;

	public DailyPriceHelper(DB db, MongoDBLogger logger) {
		this.logger = logger;
		this.dbDailyPriceCollection = db.getCollection("dailyPrice");
	}

	public void insert(DummyTickPrice tickPrice) {
		logger.logInsert(tickPrice);
		dbDailyPriceCollection.insert(tickPrice.toDBObject());
	}

	public DBObject queryById(String ticker, String date) {
		DBObject query = new BasicDBObject("_id", DailyPrice.createKey(ticker, date));
		DBCursor cursor = dbDailyPriceCollection.find(query);
		DBObject result = cursor.one();
		DailyPrice dailyPrice = new DailyPrice();
		dailyPrice.fromDBObject(result);
		return result;
	}
}
