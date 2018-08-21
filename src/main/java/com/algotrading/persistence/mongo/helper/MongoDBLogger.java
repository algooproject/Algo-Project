package com.algotrading.persistence.mongo.helper;

import com.algotrading.interactivebroker.util.Logger;

public class MongoDBLogger {

	private final Logger logger;

	public MongoDBLogger(Logger logger) {
		this.logger = logger;
	}

	protected void logInsert(Object object) {
		logger.info("Inserting: " + object);
	}
}
