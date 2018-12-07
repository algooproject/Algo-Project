package com.algotrading.persistence.mongo.dbobject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class AccountValue implements DBObjectable {

	public static final String FIELD_ACCOUNT_NAME = "accountName";
	public static final String FIELD_ACCOUNT_FIELD_NAME = "accountFieldName";

	private String accountName;
	private Map<String, AccountFieldValue> accountFieldValues = new HashMap<>();

	public String getAccountName() {
		return accountName;
	}

	@Override
	public String getKey() {
		return accountName;
	}

	@Override
	public DBObject toDBObject() {
		BasicDBObject dbObject = new BasicDBObject("_id", getKey()).append(FIELD_ACCOUNT_NAME, getAccountName());
		BasicDBObject accountFieldNameObject = new BasicDBObject();
		for (Map.Entry<String, AccountFieldValue> entry : accountFieldValues.entrySet()) {
			BasicDBObject object = new BasicDBObject(AccountFieldValue.FIELD_VALUE, entry.getValue()
					.getValue()).append(AccountFieldValue.FIELD_CURRENCY,
							entry.getValue()
									.getCurrency());
			accountFieldNameObject.append(entry.getKey(), object);
		}
		dbObject.append(FIELD_ACCOUNT_FIELD_NAME, accountFieldNameObject);
		return dbObject;
	}

	@Override
	public void fromDBObject(DBObject dbObject) {
		accountName = (String) dbObject.get(FIELD_ACCOUNT_NAME);
		BasicDBObject accountFieldNameDBObject = (BasicDBObject) dbObject.get(FIELD_ACCOUNT_FIELD_NAME);

		for (String key : accountFieldNameDBObject.keySet()) {
			BasicDBObject accountFieldValueDBObject = (BasicDBObject) accountFieldNameDBObject.get(key);
			AccountFieldValue value = new AccountFieldValue();
			value.setCurrency(accountFieldValueDBObject.getString(AccountFieldValue.FIELD_CURRENCY));
			value.setValue(accountFieldValueDBObject.getString(AccountFieldValue.FIELD_VALUE));
			accountFieldValues.put(key, value);
		}
	}

	@Override
	public String getCollectionName() {
		return "accountValue";
	}

	@Override
	public Supplier<? extends DBObjectable> getSupplier() {
		// TODO Auto-generated method stub
		return AccountValue::new;
	}

	public Map<String, AccountFieldValue> getAccountFieldValues() {
		return accountFieldValues;
	}

	public void setAccountFieldValues(Map<String, AccountFieldValue> accountFieldValues) {
		this.accountFieldValues = accountFieldValues;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Override
	public AccountValue clone() {
		AccountValue accountValueClone = new AccountValue();
		accountValueClone.setAccountName(accountName);

		Map<String, AccountFieldValue> accountFieldValuesClone = new HashMap<>();
		for (Map.Entry<String, AccountFieldValue> entry : accountFieldValues.entrySet()) {
			AccountFieldValue accountFieldValueClone = new AccountFieldValue();
			accountFieldValueClone.setValue(entry.getValue()
					.getValue());
			accountFieldValueClone.setCurrency(entry.getValue()
					.getCurrency());
			accountFieldValuesClone.put(entry.getKey(), accountFieldValueClone);
		}
		accountValueClone.setAccountFieldValues(accountFieldValuesClone);
		return accountValueClone;
	}
}
