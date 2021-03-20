package com.algotrading.backtesting.replay.test;

import com.algotrading.backtesting.replay.ActiveTradedStocksReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.algotrading.constants.TestConstants.*;

public class ActiveTradedStocksReaderTest {

    public static final String VALUE_20130101 = "20130101";
    public static final String VALUE_20140101 = "20140101";

    private String folderPathStr;

    @Before
    public void setup() throws IOException {
        Path folderPath = Files.createTempDirectory("folder");
        folderPathStr = folderPath.toString();
        Path file1Path = folderPath.resolve("20130101.txt");
        List<String> lines1 = Arrays.asList(HK_0001, HK_0002, HK_0003);
        Files.write(file1Path, lines1, StandardCharsets.UTF_8);

        Path file2Path = folderPath.resolve("20140101.txt");
        List<String> lines2 = Arrays.asList(HK_0004, HK_0005, HK_0006);
        Files.write(file2Path, lines2, StandardCharsets.UTF_8);

        folderPath.toFile().deleteOnExit();
        file1Path.toFile().deleteOnExit();
        file2Path.toFile().deleteOnExit();
    }

    @Test
    public void read() throws IOException {
        ActiveTradedStocksReader reader = new ActiveTradedStocksReader(folderPathStr);
        Map<String, List<String>> output = reader.constructYearToTickerListFromFiles();
        Assert.assertEquals(2, output.size());

        List<String> file20130101 = output.get(VALUE_20130101);
        Assert.assertEquals(3, file20130101.size());
        Assert.assertEquals(HK_0001, file20130101.get(0));
        Assert.assertEquals(HK_0002, file20130101.get(1));
        Assert.assertEquals(HK_0003, file20130101.get(2));

        List<String> file20140101 = output.get(VALUE_20140101);
        Assert.assertEquals(3, file20140101.size());
        Assert.assertEquals(HK_0004, file20140101.get(0));
        Assert.assertEquals(HK_0005, file20140101.get(1));
        Assert.assertEquals(HK_0006, file20140101.get(2));

    }
}
