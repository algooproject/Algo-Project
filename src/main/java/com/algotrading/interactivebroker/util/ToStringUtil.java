package com.algotrading.interactivebroker.util;

import com.ib.client.TickAttr;

/** Util to make toString for the struct IB provided */
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
