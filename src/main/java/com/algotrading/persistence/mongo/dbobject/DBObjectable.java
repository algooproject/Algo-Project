package com.algotrading.persistence.mongo.dbobject;

import java.util.function.Supplier;

import com.mongodb.DBObject;

/**
 * Please implements this interface for the object who will become DBObject in
 * mongodb format.
 */
public interface DBObjectable {

	String ID = "_id";

	DBObject toDBObject();

	void fromDBObject(DBObject dbObject);

	String getKey();

	String getCollectionName();

	Supplier<? extends DBObjectable> getSupplier();

}
