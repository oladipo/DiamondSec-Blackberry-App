package com.synkron.diamondsec;

import com.synkron.diamondsec.connectors.InfoWareConnector;
import com.synkron.diamondsec.connectors.TradingConfirmationConnector;
import com.synkron.diamondsec.entities.*;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;

public class TradingConfirmationScreen extends SubScreen {
	Stock _theStock;
	Trade _theTrade;
	
	VerticalFieldManager _vManager;
	HorizontalFieldManager _hManagerSummary, _hManagerOrderType, _hManagerLimitCondition,
			_hManagerPrice, _hManagerAmount,_hManagerDuration, _hManagerPIN;
	
	CustomLabelField _lblSummary, _lblEquityName, _lblOrderType, _lblOrderTypeValue,
	       _lblLimitCondition, _lblLimitConditionValue, _lblPrice, _lblPriceValue,
	       _lblAmount,_lblAmountValue,_lblDuration,_lblDurationValue, _lblPin;
	
	TextField _txtPIN; 
	CustomButtonField _btnSubmit;
	
	public TradingConfirmationScreen(Stock theStock, Trade theTrade) {
		this();
		_theStock = theStock;
		_theTrade = theTrade;
		SetupConfirmationFields();
	}
	
	public TradingConfirmationScreen() {
		super();
		_hManager.add(new CustomButtonField("Trading", Color.DARKBLUE));
		setupStatusCommands();
	}

	public void SetupConfirmationFields()
	{
		_vManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL| USE_ALL_WIDTH);
		
		_hManagerSummary = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerSummary.setBackground(BackgroundFactory.createSolidBackground(Color.DARKBLUE));
		_hManagerSummary.setMargin(20,20,0,20);
		
		_lblSummary = new CustomLabelField();
		_lblSummary.setFontColor(Color.WHITE);
		_lblSummary.setMargin(20,0,20,20);
		_lblSummary.setText("Buy or Sell");
		
		_lblEquityName = new CustomLabelField();
		_lblEquityName.setFontColor(Color.YELLOW);
		_lblEquityName.setMargin(20,20,20,20);
		_lblEquityName.setText(_theStock._name);
		
		_hManagerSummary.add(_lblSummary);
		_hManagerSummary.add(_lblEquityName);
		
		_lblOrderType = new CustomLabelField();
		_lblOrderType.setFontColor(Color.WHITE);
		_lblOrderType.setMargin(20,0,5,20);
		_lblOrderType.setText("Order Type");
		
		_lblOrderTypeValue = new CustomLabelField();
		_lblOrderTypeValue.setFontColor(Color.WHITE);
		_lblOrderTypeValue.setMargin(20,0,5,20);
		_lblOrderTypeValue.setText(_theTrade.strOrderType);
		
		_hManagerOrderType = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerOrderType.setMargin(10,20,0,20);
		
		_hManagerOrderType.add(_lblOrderType);
		_hManagerOrderType.add(_lblOrderTypeValue);
		
		_lblLimitCondition = new CustomLabelField();
		_lblLimitCondition.setFontColor(Color.WHITE);
		_lblLimitCondition.setMargin(20,0,5,20);
		_lblLimitCondition.setText("Limit Condition");
		
		_lblLimitConditionValue = new CustomLabelField();
		_lblLimitConditionValue.setFontColor(Color.WHITE);
		_lblLimitConditionValue.setMargin(20,0,5,20);
		_lblLimitConditionValue.setText(_theTrade.strLimitCondition);
		
		_hManagerLimitCondition = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerLimitCondition.setMargin(10,20,0,20);
		
		_hManagerLimitCondition.add(_lblLimitCondition);
		_hManagerLimitCondition.add(_lblLimitConditionValue);
		
		_lblPrice = new CustomLabelField();
		_lblPrice.setFontColor(Color.WHITE);
		_lblPrice.setMargin(20,0,5,20);
		_lblPrice.setText("Price");
		
		_lblPriceValue = new CustomLabelField();
		_lblPriceValue.setFontColor(Color.WHITE);
		_lblPriceValue.setMargin(20,0,5,20);
		_lblPriceValue.setText(_theStock._currentPrice);
		
		_hManagerPrice = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerPrice.setMargin(10,20,0,20);
		
		_hManagerPrice.add(_lblPrice);
		_hManagerPrice.add(_lblPriceValue);
		
		_lblAmount = new CustomLabelField();
		_lblAmount.setFontColor(Color.WHITE);
		_lblAmount.setMargin(20,0,5,20);
		_lblAmount.setText("Amount");
		
		_lblAmountValue = new CustomLabelField();
		_lblAmountValue.setFontColor(Color.WHITE);
		_lblAmountValue.setMargin(20,0,5,20);
		_lblAmountValue.setText(_theTrade.strAmount);
		
		_hManagerAmount = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerAmount.setMargin(10,20,0,20);
		
		_hManagerAmount.add(_lblAmount);
		_hManagerAmount.add(_lblAmountValue);
		
		_lblDuration = new CustomLabelField();
		_lblDuration.setFontColor(Color.WHITE);
		_lblDuration.setMargin(20,0,5,20);
		_lblDuration.setText("Duration");
		
		_lblDurationValue = new CustomLabelField();
		_lblDurationValue.setFontColor(Color.WHITE);
		_lblDurationValue.setMargin(20,0,5,20);
		_lblDurationValue.setText(_theTrade.strDuration);
		
		_hManagerDuration = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerDuration.setMargin(0,20,0,20);
		
		_hManagerDuration.add(_lblDuration);
		_hManagerDuration.add(_lblDurationValue);
		
		_lblPin = new CustomLabelField();
		_lblPin.setFontColor(Color.WHITE);
		_lblPin.setMargin(20,120,5,20);
		_lblPin.setText("PIN");
		
		_txtPIN = new TextField(Field.FIELD_LEADING | TextField.NO_NEWLINE);
		_txtPIN.setMaxSize(10);
		_txtPIN.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2,2,2,2)));
		_txtPIN.setMargin(20,10,5,20);
		_txtPIN.setPadding(10,10,10,10);
		_txtPIN.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		
		_hManagerPIN = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerPIN.setMargin(10,20,0,20);
		
		_hManagerPIN.add(_lblPin);
		_hManagerPIN.add(_txtPIN);
		
		_btnSubmit = new CustomButtonField("Confirm",Color.LIGHTGREEN);
		_btnSubmit.setChangeListener(new FieldChangeListener(){
			public void fieldChanged(Field field, int context) {
				String _Url = InfoWareConnector.API_PLACE_TRADE_ORDER_URL;
				_Url = _Url+_txtPIN.getText()+"|"+_theTrade.strPlaceTradeURL;
				TradingConfirmationConnector _connector = new TradingConfirmationConnector(_Url);
				_connector.start();
			}
		});
		_btnSubmit.setMargin(new XYEdges(20,0,10,0));
		
		_vManager.add(_hManagerSummary);
		_vManager.add(_hManagerOrderType);
		_vManager.add(_hManagerLimitCondition);
		_vManager.add(_hManagerPrice);
		_vManager.add(_hManagerAmount);
		_vManager.add(_hManagerDuration);
		_vManager.add(_hManagerPIN);
		_vManager.add(_btnSubmit);
		
		add(_vManager);
	}
}
