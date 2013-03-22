package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StockWatcher implements EntryPoint {

	private static final int REFRESH_INTERVAL = 5000;// ms
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		// create table for stock data
		stocksFlexTable.setText(0, 0, "Symbol");
		stocksFlexTable.setText(0, 1, "Price");
		stocksFlexTable.setText(0, 2, "Change");
		stocksFlexTable.setText(0, 3, "Remove");

		// assemble Add Stock panel
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);

		// Assemble Main panel.
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("stockList").add(mainPanel);
		// Move cursor focus to the input box.
		newSymbolTextBox.setFocus(true);
		// setup timer to refresh list automatically.
		Timer refreshTimer = new Timer() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshWatchlist();

			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

		// listen for mouse events on Add button.
		addStockButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				addStock();

			}

		});
		// listen for keyboard events in the input box.
		newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addStock();

				}

			}
		});

	}

	private void addStock() {
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		newSymbolTextBox.setFocus(true);
		// stock must be between 1 and 10 chars that are number,letters or dots
		if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
			Window.alert("'" + symbol + "' is not a valid symbol.");
			newSymbolTextBox.selectAll();
			return;
		}
		newSymbolTextBox.setText("");
		// Don't add the stock if it's already in the table.
		if (stocks.contains(symbol))
			return;

		// Add the stock to the table.
		int row = stocksFlexTable.getRowCount();
		stocks.add(symbol);
		stocksFlexTable.setText(row, 0, symbol);

		// Add a button to remove this stock from the table.
		Button removeStockButton = new Button("x");
		removeStockButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int removedIndex = stocks.indexOf(symbol);
				stocks.remove(removedIndex);
				stocksFlexTable.removeRow(removedIndex + 1);

			}
		});
		stocksFlexTable.setWidget(row, 3, removeStockButton);

		// Get the stock price.
		refreshWatchlist();

	}

	private void refreshWatchlist() {
		// generate random stock price
		 final double MAX_PRICE = 100.0;
		 final double MAX_PRICE_CHANGE = 0.02;
		 
		 StockPrice[] prices = new StockPrice[stocks.size()];
		 for (int i = 0 ; i< stocks.size(); i++){
			 double price = Random.nextDouble()* MAX_PRICE;
			 double change = price * MAX_PRICE_CHANGE * (Random.nextDouble()*2.0 -1.0);
			 prices[i]= new StockPrice(stocks.get(i), price, change);
			 
		 }
			updateTabel(prices);

	}

	private void updateTabel(StockPrice[] prices) {
		// TODO Auto-generated method stub
		
	}

}