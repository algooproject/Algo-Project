package com.algotrading.backtesting.replay;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveTradedStocksReader {

    private final String classFilePath;

    public ActiveTradedStocksReader(String classFilePath) {
        this.classFilePath = classFilePath;
    }

    public Map<String, List<String>> constructYearToTickerListFromFiles() throws IOException {
        Map<String, List<String>> yearToTickerList;
        List<Path> fileList = getStockFilePathList();
        Charset charset = Charset.defaultCharset();
        yearToTickerList = new HashMap<>();
        for( Path file : fileList ) {
            yearToTickerList.put(file.toFile().getName().substring(0, 8),
                    Files.readAllLines(file, charset));
        }
        return yearToTickerList;
    }

    private List<Path> getStockFilePathList() {
        List<Path> fileList = new ArrayList<>();
        File dir = new File(classFilePath);
        File[] files = dir.listFiles((dir1, name) -> name.matches("\\d\\d\\d\\d\\d\\d\\d\\d\\.txt"));

        assert( files != null );
        for (File file : files) {
            fileList.add(file.toPath());
        }
        return fileList;
    }
}
