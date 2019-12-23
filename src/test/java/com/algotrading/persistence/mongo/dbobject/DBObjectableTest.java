package com.algotrading.persistence.mongo.dbobject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.algotrading.interactivebroker.util.Logger;
import com.algotrading.persistence.mongo.helper.DummyTickPriceDBHelper;
import com.algotrading.persistence.mongo.helper.MongoDBLogger;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Ignore
public class DBObjectableTest {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE_EXAMPLES = "Test";
	private final MongoClient mongoClient;
	private final DB databaseExamples;
	private final MongoDBLogger mongoDbLogger;
	private final Logger logger;
	public final DummyTickPriceDBHelper dummyTickPriceHelper;

	public DBObjectableTest() {
		this.logger = new Logger();
		this.mongoClient = new MongoClient(new MongoClientURI("mongodb://" + HOSTNAME + ":" + PORT));
		this.databaseExamples = mongoClient.getDB(DATABASE_EXAMPLES);
		this.mongoDbLogger = new MongoDBLogger(logger);
		this.dummyTickPriceHelper = new DummyTickPriceDBHelper(databaseExamples, mongoDbLogger);
	}

	@Test
	public void testInsertQueryUpdateDelete() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse("20180903");
		DummyTickPrice dummyTickPrice = new DummyTickPrice(date, "ticker", 0.1);

		// Insert
		dummyTickPriceHelper.insert(dummyTickPrice);

		// Query
		DummyTickPrice queriedDummyTickPrice = dummyTickPriceHelper.queryById(dummyTickPrice.getKey());
		Assert.assertEquals(sdf.format(date), sdf.format(queriedDummyTickPrice.getDate()));
		Assert.assertEquals(0.1, queriedDummyTickPrice.getPrice(), 0.01);
		Assert.assertEquals("ticker", queriedDummyTickPrice.getTicker());

		// Update
		queriedDummyTickPrice.setPrice(0.2);
		dummyTickPriceHelper.updateById(queriedDummyTickPrice.getKey(), queriedDummyTickPrice);

		queriedDummyTickPrice = dummyTickPriceHelper.queryById(dummyTickPrice.getKey());
		Assert.assertEquals(sdf.format(date), sdf.format(queriedDummyTickPrice.getDate()));
		Assert.assertEquals(0.2, queriedDummyTickPrice.getPrice(), 0.01);
		Assert.assertEquals("ticker", queriedDummyTickPrice.getTicker());

		// Delete
		dummyTickPriceHelper.removeById(queriedDummyTickPrice.getKey());

		List<DummyTickPrice> list = dummyTickPriceHelper.queryByfield("_id", queriedDummyTickPrice.getKey());
		Assert.assertEquals(0, list.size());

		queriedDummyTickPrice = dummyTickPriceHelper.queryById(dummyTickPrice.getKey());
		Assert.assertEquals(null, queriedDummyTickPrice);
	}

	@After
	public void tearDown() {
		databaseExamples.dropDatabase();
	}
}
