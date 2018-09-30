package com.algotrading.persistence.mongo.helper;

import com.algotrading.interactivebroker.util.Logger;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBHelper {

	private final MongoClient mongoClient;
	private final DB database;
	private final MongoDBLogger mongoDbLogger;

	public final DummyTickPriceDBHelper dummyTickPrice;
	public final DailyPriceHelper dailyPrice;
	public final StockInfoHelper stockInfo;
	public final TickPriceHelper tickPrice;

	public MongoDBHelper(Logger logger, String databaseName) {
		this(logger, "localhost", 27017, databaseName);
	}

	public MongoDBHelper(Logger logger, String hostName, int port, String databaseName) {
		this.mongoClient = new MongoClient(new MongoClientURI("mongodb://" + hostName + ":" + port));
		this.database = mongoClient.getDB(databaseName);
		this.mongoDbLogger = new MongoDBLogger(logger);
		this.dummyTickPrice = new DummyTickPriceDBHelper(database, mongoDbLogger);
		this.dailyPrice = new DailyPriceHelper(database, mongoDbLogger);
		this.stockInfo = new StockInfoHelper(database, mongoDbLogger);
		this.tickPrice = new TickPriceHelper(database, mongoDbLogger);

	}

	public void dropDatabase() {
		database.dropDatabase();
	}

}
