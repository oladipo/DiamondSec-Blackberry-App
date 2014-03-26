package com.synkron.diamondsec;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TradingStatusScreen extends SubScreen implements FieldChangeListener{
	CustomButtonField _btnPortfolio, _btnStockList, _btnOrderHistory;
	VerticalFieldManager _vManager;
	CustomLabelField _lblOrderStatus;
	
	public TradingStatusScreen(String strMessage) {
		super();
		_hManager.add(new CustomButtonField("Trade",Color.DARKBLUE));
		setupStatusCommands();
		if(strMessage.equals(" ")){
			strMessage = "Your Order is Sucessfully Accepted!"; 
		}
		_vManager = new VerticalFieldManager(USE_ALL_WIDTH | Manager.FIELD_HCENTER);
		
		_lblOrderStatus = new CustomLabelField(strMessage,FIELD_HCENTER);
		_lblOrderStatus.setFontColor(Color.WHITE);
		_lblOrderStatus.setMargin(20,20,5,20);
		
		_btnPortfolio = new CustomButtonField("Check Portfolio", Color.LIGHTGREEN, CustomButtonField.THREE_FOURTH_SCREEN_WIDTH);
		_btnPortfolio.setMargin(40, 0, 0, 0);
		_btnPortfolio.setChangeListener(this);
		
		_btnStockList = new CustomButtonField("Check Stock List", Color.LIGHTGREEN, CustomButtonField.THREE_FOURTH_SCREEN_WIDTH);
		_btnStockList.setMargin(40, 0, 0, 0);
		_btnStockList.setChangeListener(this);
		
		_btnOrderHistory = new CustomButtonField("Check Order History", Color.LIGHTGREEN, CustomButtonField.THREE_FOURTH_SCREEN_WIDTH);
		_btnOrderHistory.setMargin(40, 0, 40, 0);
		_btnOrderHistory.setChangeListener(this);
		
		_vManager.add(_lblOrderStatus);
		_vManager.add(_btnPortfolio);
		_vManager.add(_btnStockList);
		_vManager.add(_btnOrderHistory);
		
		add(_vManager);
	}

	public void fieldChanged(Field field, int context) {
		UiApplication diamondSec = UiApplication.getUiApplication();
		if(field == _btnStockList){
			diamondSec.pushScreen(new StockListScreen());
		}
		if(field == _btnOrderHistory){
			diamondSec.pushScreen(new OrderHistoryScreen());
		}
	}

}
