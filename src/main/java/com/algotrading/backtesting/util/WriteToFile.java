package com.algotrading.backtesting.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {

	public void buyStrategies(String strFileName, int intRSIMagnitude, double dblRSILowerThan, int intSMAMagnitude,
			double dblVolumeHigherThan, int intReentryRSIMagnitude, double dblReentryRSILowerThan) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(strFileName));

		writer.write("AND( OR( isStockEnabled[ ] , Reentry( RSIHigher[ magnitude=");
		writer.write(Integer.toString(intReentryRSIMagnitude));
		writer.write(" sma_magnitude=0 expectedValueType=number expectedValue=");
		writer.write(Double.toString(dblReentryRSILowerThan));
		writer.write(" ] ) ) , AND( AND( AND( AND( RSILower[ magnitude=");
		writer.write(Integer.toString(intRSIMagnitude));
		writer.write(" sma_magnitude=0");
		writer.write(" expectedValueType=number expectedValue=");
		writer.write(Double.toString(dblRSILowerThan));
		writer.write(" ] , SMAHigher[ magnitude=");
		writer.write(Integer.toString(intSMAMagnitude));
		writer.write(
				" expectedValueType=variable expectedValue=closing ] ) , VolumeHigher[ expectedValueType=variable expectedValue=volume expectedLag=1 multiplier=");
		writer.write(Double.toString(dblVolumeHigherThan));
		writer.write(
				" ] ) , PositionEqual[ expectedValueType=number expectedValue=0 ] ) , isSufficientCash[ ] ) )	100000");

		writer.close();
	}

	public void reentryStrategies(String strFileName, int intRSIMagnitude, double dblRSILowerThan) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(strFileName));
		writer.write("AND( NOT( RSILower[ magnitude=");
		writer.write(Integer.toString(intRSIMagnitude));
		writer.write(" sma_magnitude=0 expectedValueType=number expectedValue=");
		writer.write(Double.toString(dblRSILowerThan));
		writer.write(" ] )	0");
		writer.close();
	}

	public void sellStrategies(String strFileName, double dblTakeProfit, double dblStopLoss) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(strFileName));
		writer.write(
				"AND( NOT( PositionEqual[ expectedValueType=number expectedValue=0 ] ) , OR( ClosingHigher[ expectedValueType=variable expectedValue=holdingprice multiplier=");
		writer.write(Double.toString(dblTakeProfit));
		writer.write(
				" ] , Exit( NOT( ClosingHigher[ expectedValueType=variable expectedValue=holdingprice multiplier=");
		writer.write(Double.toString(dblStopLoss));
		writer.write(" ] ) ) ) )	0");
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
