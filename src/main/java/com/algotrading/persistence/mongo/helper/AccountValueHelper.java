package com.algotrading.persistence.mongo.helper;

import com.algotrading.persistence.mongo.dbobject.AccountValue;
import com.mongodb.DB;

public class AccountValueHelper extends DBObjectableHelper<AccountValue> {

	public AccountValueHelper(DB db, MongoDBLogger logger) {
		super(db, logger, new AccountValue());
	}
}
