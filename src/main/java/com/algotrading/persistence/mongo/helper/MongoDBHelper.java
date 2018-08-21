package com.algotrading.persistence.mongo.helper;

import com.algotrading.interactivebroker.util.Logger;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBHelper {

	private final MongoClient mongoClient;
	private final DB databaseExamples;

	public TickPriceDBHelper tickPrice;

	public MongoDBHelper(Logger logger) {
		this(logger, "localhost", 27017);
	}

	public MongoDBHelper(Logger logger, String hostName, int port) {
		this.mongoClient = new MongoClient(new MongoClientURI("mongodb://" + hostName + ":" + port));
		this.databaseExamples = mongoClient.getDB("Examples");
		this.tickPrice = new TickPriceDBHelper(databaseExamples, new MongoDBLogger(logger));
	}

}
