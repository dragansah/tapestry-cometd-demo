package org.lazan.t5.cometddemo.stocks;

public class StockPrice {
	private String ticker;
	private double price;
	public StockPrice(String ticker, double price) {
		super();
		this.ticker = ticker;
		this.price = price;
	}
	public String getTicker() {
		return ticker;
	}
	public double getPrice() {
		return price;
	}
}
