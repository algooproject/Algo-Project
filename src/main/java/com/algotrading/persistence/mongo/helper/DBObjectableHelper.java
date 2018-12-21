package com.algotrading.persistence.mongo.helper;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.algotrading.persistence.mongo.dbobject.DBObjectable;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class DBObjectableHelper<DBOBJECTABLE extends DBObjectable> {
	protected final MongoDBLogger logger;
	protected final DBCollection databaseCollection;
	protected final Supplier<DBOBJECTABLE> dbobjectSupplier;

	public DBObjectableHelper(DB database, MongoDBLogger logger, DBOBJECTABLE dbObject) {
		this.logger = logger;
		this.databaseCollection = database.getCollection(dbObject.getCollectionName());
		dbobjectSupplier = (Supplier<DBOBJECTABLE>) dbObject.getSupplier();
	}

	public void insert(DBOBJECTABLE dbObject) {
		logger.logInsert(dbObject);
		databaseCollection.insert(dbObject.toDBObject());
	}

	protected DBOBJECTABLE fromDBObject(DBObject dbObject) {
		if (dbObject == null) {
			return null;
		}
		DBOBJECTABLE dbObjectable = dbobjectSupplier.get();
		dbObjectable.fromDBObject(dbObject);
		return dbObjectable;
	}

	public DBOBJECTABLE queryById(String id) {
		DBObject query = new BasicDBObject(DBObjectable.ID, id);
		DBCursor cursor = databaseCollection.find(query);
		DBObject result = cursor.one();
		return fromDBObject(result);
	}

	public boolean existsById(String id) {
		DBObject query = new BasicDBObject(DBObjectable.ID, id);
		DBCursor cursor = databaseCollection.find(query);
		DBObject result = cursor.one();
		return result != null;
	}

	public List<DBOBJECTABLE> queryByfield(String fieldName, Object fieldValue) {
		return queryByField(fieldName, fieldValue, null, null, null);
	}

	public List<DBOBJECTABLE> queryByField(String fieldName, Object fieldValue, DBObject sortBy, Integer skipNumElement,
			Integer limitNumElement) {
		DBObject query = new BasicDBObject(fieldName, fieldValue);
		DBCursor cursor = databaseCollection.find(query);
		if (fieldValue != null) {
			cursor = cursor.sort(sortBy);
		}
		if (skipNumElement != null) {
			cursor = cursor.skip(skipNumElement);
		}
		if (limitNumElement != null) {
			cursor = cursor.limit(limitNumElement);
		}
		List<DBObject> dbObjects = cursor.toArray();
		return dbObjects.stream()
				.map(dbObject -> fromDBObject(dbObject))
				.collect(Collectors.toList());
	}

	public void updateById(String id, DBOBJECTABLE dbObjectable) {
		DBObject queryObject = new BasicDBObject(DBObjectable.ID, id);
		WriteResult resultOfUpdate = databaseCollection.update(queryObject, dbObjectable.toDBObject());
	}

	/**
	 * creates a new document when no document matches the query criteria.
	 */
	public void upsertById(String id, DBOBJECTABLE dbObjectable) {
		DBObject queryObject = new BasicDBObject(DBObjectable.ID, id);
		WriteResult resultOfUpdate = databaseCollection.update(queryObject, dbObjectable.toDBObject(), true, false);
	}

	public void removeById(String id) {
		DBObject queryObject = new BasicDBObject(DBObjectable.ID, id);
		// TODO execute the remove
		WriteResult resultOfRemove = databaseCollection.remove(queryObject);
	}

}
