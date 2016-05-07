package com.roulesophy.stockanalysis.model.indicatorcalculator;

import java.math.BigDecimal;
import java.util.Date;

public interface IRsiCalculator {
	public BigDecimal calculate(Date date, int scale);
}
