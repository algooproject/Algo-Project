package com.algotrading.backtesting.replay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class LotSize {

	private String file;
	private Map<String, Integer> lotSizes;

	public LotSize() {
		lotSizes = new HashMap<String, Integer>();
	}

	public LotSize(String file) throws IOException, ParseException {
		this.file = file;
		lotSizes = new HashMap<String, Integer>();
		read();
	}

	public int getLotSize(String strStockCode) {
		// System.out.println(strStockCode);
		return lotSizes.get(strStockCode);
	}

	public void read() throws IOException, ParseException {
		String strLine = "";
		String strCvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((strLine = br.readLine()) != null) {
				// System.out.println(strLine);
				// use comma as separator
				String[] strLotSize = strLine.split(strCvsSplitBy);
				String strStockCode = strLotSize[0];
				int intLotSize = Integer.parseInt(strLotSize[1]);
				lotSizes.put(strStockCode, intLotSize);
				// System.out.println(strStockCode + " " + intLotSize);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
