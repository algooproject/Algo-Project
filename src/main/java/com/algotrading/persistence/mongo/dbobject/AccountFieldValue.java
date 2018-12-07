package com.algotrading.persistence.mongo.dbobject;

public class AccountFieldValue {
	public static String FIELD_VALUE = "value";
	public static String FIELD_CURRENCY = "currency";

	private String value;
	private String currency;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "AccountFieldValue [value=" + value + ", currency=" + currency + "]";
	}

}