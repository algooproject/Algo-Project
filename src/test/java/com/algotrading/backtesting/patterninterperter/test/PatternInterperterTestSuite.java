package com.algotrading.backtesting.patterninterperter.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//@formatter:off
@SuiteClasses({ 
	CashInterpreterTest.class, 
	InterperterTest.class, 
	PositionEqualInterpreterTest.class, 
	SmaHigherThanInterpreterTest.class 
})
//@formatter:on
public class PatternInterperterTestSuite {

}