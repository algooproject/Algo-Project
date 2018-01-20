package com.algotrading.interactivebroker.builder;

import org.junit.Assert;
import org.junit.Test;

import com.algotrading.interactivebroker.builder.ContractBuilder;
import com.ib.client.Contract;
import com.ib.client.Types.SecType;

public class ContractBuilderTest {

	@Test
	public void testContractBuilder() {
		Contract contract = new ContractBuilder().symbol("IBKR")
				.secType(SecType.STK)
				.currency("USD")
				.exchange("ISLAND")
				.build();
		Assert.assertEquals("IBKR", contract.symbol());
		Assert.assertEquals(SecType.STK, contract.secType());
		Assert.assertEquals("USD", contract.currency());
		// In the API side, NASDAQ is always defined as ISLAND in the exchange
		Assert.assertEquals("ISLAND", contract.exchange());
	}

	@Test
	public void testContractBuilderStringSecType() {
		Contract contract = new ContractBuilder().symbol("IBKR")
				.secType("STK")
				.currency("USD")
				.exchange("ISLAND")
				.build();
		Assert.assertEquals("IBKR", contract.symbol());
		Assert.assertEquals(SecType.STK, contract.secType());
		Assert.assertEquals("USD", contract.currency());
		// In the API side, NASDAQ is always defined as ISLAND in the exchange
		Assert.assertEquals("ISLAND", contract.exchange());
	}

}
