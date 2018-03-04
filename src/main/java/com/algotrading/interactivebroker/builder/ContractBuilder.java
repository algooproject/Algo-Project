package com.algotrading.interactivebroker.builder;

import com.ib.client.Contract;
import com.ib.client.Types.SecType;

public class ContractBuilder {

	private final Contract contract;

	public ContractBuilder() {
		contract = new Contract();
	}

	public Contract build() {
		return contract;
	}

	public ContractBuilder symbol(String ticker) {
		contract.symbol(ticker);
		return this;
	}

	public ContractBuilder secType(SecType securityType) {
		contract.secType(securityType);
		return this;
	}

	public ContractBuilder secType(String securityType) {
		contract.secType(securityType);
		return this;
	}

	public ContractBuilder currency(String currency) {
		contract.currency(currency);
		return this;
	}

	public ContractBuilder exchange(String exchange) {
		contract.exchange(exchange);
		return this;
	}

	private Contract getContract() {
		Contract contract = new Contract();
		contract.symbol("IBKR");
		contract.secType("STK");
		contract.currency("USD");
		// In the API side, NASDAQ is always defined as ISLAND in the exchange
		// field
		contract.exchange("ISLAND");
		return contract;
	}
}
