package com.algotrading.backtesting.stock.io;

import com.algotrading.backtesting.stock.Stock;
import com.algotrading.backtesting.stock.StockHistory;
import com.algotrading.backtesting.util.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class StockFileGateway implements StockGateway {

    private final String filePath;

    public StockFileGateway(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean fillData(Stock stock) {
        return readFromFile(stock, filePath, true);
    }

    public boolean readFromFile(Stock stock, String filePath, boolean withHeader) {
        // TODO read files from ticker
        String strCsvFile = filePath + stock.getTicker() + ".csv";
        String strLine = "";
        String strCvsSplitBy = ",";
        boolean isFirstLine = withHeader; // will skip header if true
        Double dbLastClose = null;
        Double dbLastAdjClose = null;

        try (BufferedReader br = new BufferedReader(new FileReader(strCsvFile))) {
            while ((strLine = br.readLine()) != null) {
                // System.out.println(strLine);
                if (isFirstLine) {
                    isFirstLine = false;
                } else {
                    // use comma as separator
                    String[] strStockHistory = strLine.split(strCvsSplitBy);
                    // System.out.println(strStockHistory[0] + '|' +
                    // strStockHistory[1] + '|' + strStockHistory[2] + '|' +
                    // strStockHistory[3] + '|' + strStockHistory[4] + '|' +
                    // strStockHistory[5] + '|' + strStockHistory[6]);
                    Date dtStockHistoryDate = Constants.DATE_FORMAT_YYYYMMDD.parse(strStockHistory[0]);
                    Double dbOpen = null;
                    Double dbClose = null;
                    Double dbHigh = null;
                    Double dbLow = null;
                    Double dbAdjClose = null;
                    Double dbVolume = null;
                    if (strStockHistory[1].equals("null")) {
                        dbOpen = dbLastClose;
                        dbClose = dbLastClose;
                        dbHigh = dbLastClose;
                        dbLow = dbLastClose;
                        dbAdjClose = dbLastAdjClose;
                        dbVolume = 0.0;
                    } else {
                        dbOpen = Double.parseDouble(strStockHistory[1]);
                        dbClose = Double.parseDouble(strStockHistory[4]);
                        dbHigh = Double.parseDouble(strStockHistory[2]);
                        dbLow = Double.parseDouble(strStockHistory[3]);
                        dbAdjClose = Double.parseDouble(strStockHistory[5]);
                        dbVolume = Double.parseDouble(strStockHistory[6]);
                        dbLastClose = dbClose;
                        dbLastAdjClose = dbAdjClose;
                    }
                    stock.getHistory().put(dtStockHistoryDate,
                            new StockHistory(dtStockHistoryDate, dbOpen, dbClose, dbHigh, dbLow, dbAdjClose, dbVolume));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return false;
        }
        stock.initialDate();
        return true;
    }
}
