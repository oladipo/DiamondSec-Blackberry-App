package com.synkron.diamondsec;

import com.synkron.diamondsec.entities.Stock;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.*;

public class TradingScreen extends SubScreen implements FieldChangeListener{
	CustomButtonField _btnBuy, _btnSell, _btnOrderHistory;
	VerticalFieldManager _vManager;
	public Stock _myStock;
	
	public TradingScreen() {
		super();
		_hManager.add(new CustomButtonField("Trading", Color.DARKBLUE));
		super.setupStatusCommands();
		
		SetupTradingArea();
	}
	
	public TradingScreen(Stock myStock) {
		this();
		_myStock = myStock;
	}
	
	public void SetupTradingArea(){
		_vManager = new VerticalFieldManager(USE_ALL_WIDTH | Manager.FIELD_HCENTER);
		
		_btnBuy = new CustomButtonField("Buy", Color.LIGHTGREEN, CustomButtonField.THREE_FOURTH_SCREEN_WIDTH);
		_btnBuy.setMargin(40, 0, 0, 0);
		_btnBuy.setChangeListener(this);
		
		_btnSell = new CustomButtonField("Sell", Color.LIGHTGREEN, CustomButtonField.THREE_FOURTH_SCREEN_WIDTH);
		_btnSell.setMargin(40, 0, 0, 0);
		_btnSell.setChangeListener(this);
		
		_btnOrderHistory = new CustomButtonField("Order History", Color.LIGHTGREEN, CustomButtonField.THREE_FOURTH_SCREEN_WIDTH);
		_btnOrderHistory.setMargin(40, 0, 0, 0);
		_btnOrderHistory.setChangeListener(this);
		
		_vManager.add(_btnBuy);
		_vManager.add(_btnSell);
		_vManager.add(_btnOrderHistory);
		
		add(_vManager);
	}

	public void fieldChanged(Field field, int context) {
		UiApplication diamondSec = UiApplication.getUiApplication();
		
		if(field == _btnBuy){
			diamondSec.pushScreen(new TradingBuyScreen(_myStock));
		}
		if(field == _btnSell){
			diamondSec.pushScreen(new TradingSellScreen(_myStock));
		}
		if(field == _btnOrderHistory){
			diamondSec.pushScreen(new OrderHistoryScreen());
		}
	}
}
