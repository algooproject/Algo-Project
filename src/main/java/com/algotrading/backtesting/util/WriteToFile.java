package com.algotrading.backtesting.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {

	public void buyStrategies(String strFileName, int intRSIMagnitude, double dblRSILowerThan, int intSMAMagnitude,
			double dblVolumeHigherThan) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(strFileName));
		writer.write("AND( AND( AND( AND( RSILower[ magnitude=");
		writer.write(Integer.toString(intRSIMagnitude));
		writer.write(" sma_magnitude=0 expectedValueType=number expectedValue=");
		writer.write(Double.toString(dblRSILowerThan));
		writer.write(" ] , SMAHigher[ magnitude=");
		writer.write(Integer.toString(intSMAMagnitude));
		writer.write(" expectedValueType=variable expectedValue=closing ] ) , ");
		writer.write("VolumeHigher[ expectedValueType=variable expectedValue=volume expectedLag=1 multiplier=");
		writer.write(Double.toString(dblVolumeHigherThan));
		writer.write(" ] ) , ");
		writer.write("PositionEqual[ expectedValueType=number expectedValue=0 ] ) , isSufficientCash[ ] )	100000");
		writer.close();
	}

	public void reentryStrategies(String strFileName, int intRSIMagnitude, double dblRSILowerThan) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(strFileName));
		writer.write("NOT( RSILower[ magnitude=");
		writer.write(Integer.toString(intRSIMagnitude));
		writer.write(" sma_magnitude=0 expectedValueType=number expectedValue=");
		writer.write(Double.toString(dblRSILowerThan));
		writer.write(" ] )	0");
		writer.close();
	}

	public void sellStrategies(String strFileName, double dblTakeProfit) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(strFileName));
		writer.write("AND( ClosingHigher[ expectedValueType=variable expectedValue=holdingprice multiplier=");
		writer.write(Double.toString(dblTakeProfit));
		writer.write(" ] , NOT( PositionEqual[ expectedValueType=number expectedValue=0 ] ) )	0");
		writer.close();
	}

	public void exitStrategies(String strFileName, double dblStopLoss) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(strFileName));
		writer.write("NOT( ClosingHigher[ expectedValueType=variable expectedValue=holdingprice multiplier=");
		writer.write(Double.toString(dblStopLoss));
		writer.write(" ] )	0");
		writer.close();
	}

}
