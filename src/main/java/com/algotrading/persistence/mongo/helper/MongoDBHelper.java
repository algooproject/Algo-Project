package com.algotrading.persistence.mongo.helper;

import com.algotrading.interactivebroker.util.Logger;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBHelper {

	private static final String DATABASE_EXAMPLES = "Examples";
	private final MongoClient mongoClient;
	private final DB databaseExamples;
	private final MongoDBLogger mongoDbLogger;

	public final TickPriceDBHelper tickPrice;
	public final DailyPriceHelper dailyPrice;

	public MongoDBHelper(Logger logger) {
		this(logger, "localhost", 27017);
	}

	public MongoDBHelper(Logger logger, String hostName, int port) {
		this.mongoClient = new MongoClient(new MongoClientURI("mongodb://" + hostName + ":" + port));
		this.databaseExamples = mongoClient.getDB(DATABASE_EXAMPLES);
		this.mongoDbLogger = new MongoDBLogger(logger);
		this.tickPrice = new TickPriceDBHelper(databaseExamples, mongoDbLogger);
		this.dailyPrice = new DailyPriceHelper(databaseExamples, mongoDbLogger);
	}

}
