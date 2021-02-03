package com.algotrading.backtesting;

import com.algotrading.backtesting.config.AlgoConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.Map;

import static com.algotrading.backtesting.config.AlgoConfiguration.READ_AVAILABLE_STOCK_FROM;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.algotrading.backtesting.config.AlgoConfiguration")
@PrepareForTest({AlgoConfiguration.class})
public class PowerMockitoTest {
    private Map<String, String> aaa;

    @Before
    public void setUp () {
        aaa = new HashMap<>();
        aaa.put(READ_AVAILABLE_STOCK_FROM, "bbb");
        Whitebox.setInternalState(AlgoConfiguration.class, aaa);
    }

    @Test
    public void testField() {
        Assert.assertEquals("bbb", AlgoConfiguration.getReadAvailableStockFrom());
    }

}
