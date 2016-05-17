package com.algotrading.backtesting.patterninterperter;

import com.algotrading.backtesting.pattern.IStockPattern;

public interface Node {
	void parse(Context context) throws ParseException;

	IStockPattern execute();
}

//<expr> ::= <and> | <or> | <not> | <se>
//<and> ::= AND( <expr> , <expr> )
//<or> ::= OR( <expr> , <expr> )
//<not> ::= NOT( <expr> )
//<se> :: SE[ a ]
