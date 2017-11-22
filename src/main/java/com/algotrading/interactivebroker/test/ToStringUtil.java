package com.algotrading.interactivebroker.test;

import com.ib.client.TickAttr;

public class ToStringUtil {

	public static String strTickAttr(TickAttr tickAttr) {
		return new StringBuilder().append("[")
				.append("canAutoExecute=")
				.append(tickAttr.canAutoExecute())
				.append(", ")
				.append("pastLimit=")
				.append(tickAttr.pastLimit())
				.append(", ")
				.append("preOpen=")
				.append(tickAttr.preOpen())
				.append("]")
				.toString();
	}

}
