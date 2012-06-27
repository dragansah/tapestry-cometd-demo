package org.lazan.t5.cometddemo.pages;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Property;
import org.lazan.t5.cometddemo.stocks.StockPrice;

public class Stocks {
	private static final String[] TICKERS = { "GOOG", "YAHOO", "IBM", "SONY" };
	
	@Property
	private StockPrice stockPrice;
	
	@Inject
	private Block stockPriceBlock;
	
	@Inject
	private Block listBlock;
	
	
	@Property
	private String ticker;
	
	public String[] getTickers() {
		return TICKERS;
	}
	
	public String getTopic() {
		return "/stocks/" + ticker;
	}
	
	public Block onStockPriceReceived(StockPrice stockPrice) {
		this.stockPrice = stockPrice;
		return stockPriceBlock;
	}

	public Block onUpdateList(StockPrice stockPrice) {
		this.stockPrice = stockPrice;
		return listBlock;
	}
}
