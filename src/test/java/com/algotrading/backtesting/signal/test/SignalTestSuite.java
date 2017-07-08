package com.algotrading.backtesting.signal.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//@formatter:off
@SuiteClasses({ 
	PositionEqualSignalTest.class, 
	SmaHigherThanSignalTest.class
})
//@formatter:on
public class SignalTestSuite {

}