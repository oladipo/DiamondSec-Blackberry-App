package com.synkron.diamondsec;

import java.util.Date;

import com.samples.toolkit.ui.component.LabeledSwitch;
import com.synkron.diamondsec.connectors.InfoWareConnector;
import com.synkron.diamondsec.connectors.TradingBuyConnector;
import com.synkron.diamondsec.entities.*;
import com.synkron.diamondsec.utils.DataContext;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.text.TextFilter;

public class TradingBuyScreen extends SubScreen implements FieldChangeListener{
	
	VerticalFieldManager _vManager;
	HorizontalFieldManager _hManagerEquity, _hManagerPrice, 
		_hManagerFunds ,_hManagerOrder, _hManagerLimit, 
		_hManagerPrice2, _hManagerAmount, _hManagerDuration;
	CustomLabelField _lblTicker, _lblEquityName,_lblCurrentPrice, _lblPriceCurrent,
	_lblFundsAvailable, _lblAvailableFunds, _lblPrice, _lblAmount, _lblDuration;
	
	CustomButtonField _btnConfirm;
	TextField _txtFieldPrice, _txtFieldAmount;
	
	LabeledSwitch _orderSwitch, _limitCondition;
	
	ObjectChoiceField _objChoiceDuration;
	Stock _theStock;
	
	public TradingBuyScreen(Stock theStock) {
		this();
		_theStock = theStock;		
		SetupTradingScreen();
	}
	public TradingBuyScreen() {
		super();
		_hManager.add(new CustomButtonField("Trading", Color.DARKBLUE));
		setupStatusCommands();
	}

	public void SetupTradingScreen(){

		_vManager = new VerticalFieldManager();
		
		_hManagerEquity = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerEquity.setBackground(BackgroundFactory.createSolidBackground(Color.DARKBLUE));
		_hManagerEquity.setMargin(20,20,0,20);
		
		_hManagerPrice = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerPrice.setBackground(BackgroundFactory.createSolidBackground(Color.DARKBLUE));
		_hManagerPrice.setMargin(0,20,0,20);
		
		_hManagerFunds = new HorizontalFieldManager(USE_ALL_WIDTH);
		_hManagerFunds.setBackground(BackgroundFactory.createSolidBackground(Color.BLUE));
		_hManagerFunds.setMargin(0,20,0,20);
		
		_hManagerOrder = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerOrder.setMargin(0,20,0,20);
		
		_hManagerLimit = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerLimit.setMargin(0,20,0,20);
		
		_hManagerPrice2 = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerPrice2.setMargin(0,20,0,20);
		
		_hManagerAmount	= new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerAmount.setMargin(0,20,0,20);
		
		_hManagerDuration = new HorizontalFieldManager(Field.FIELD_VCENTER | USE_ALL_WIDTH);
		_hManagerDuration.setMargin(0,20,0,20);
		
		_lblTicker = new CustomLabelField();
		_lblTicker.setFontColor(Color.YELLOW);
		_lblTicker.setMargin(20,0,0,20);
		_lblTicker.setText(_theStock._ticker);
		
		_lblEquityName = new CustomLabelField();
		_lblEquityName.setFontColor(Color.YELLOW);
		_lblEquityName.setMargin(20,0,0,20);
		_lblEquityName.setText(_theStock._name);
		
		_lblCurrentPrice = new CustomLabelField();
		_lblCurrentPrice.setFontColor(Color.WHITE);
		_lblCurrentPrice.setMargin(20,0,0,20);
		_lblCurrentPrice.setText("Current Price");
		
		_lblPriceCurrent = new CustomLabelField();
		_lblPriceCurrent.setFontColor(Color.WHITE);
		_lblPriceCurrent.setMargin(20,0,0,20);
		_lblPriceCurrent.setText(_theStock._currentPrice);

		_lblFundsAvailable = new CustomLabelField();
		_lblFundsAvailable.setFontColor(Color.WHITE);
		//_lblFundsAvailable.setFont(font);
		_lblFundsAvailable.setMargin(20,5,0,20);
		_lblFundsAvailable.setText("Funds Available \nfor Trading");
		
		_lblAvailableFunds = new CustomLabelField();
		_lblAvailableFunds.setFontColor(Color.WHITE);
		//_lblAvailableFunds.setFont(font);
		_lblAvailableFunds.setMargin(20,10,0,0);
		_lblAvailableFunds.setText("9,999.00");
		
		_lblAmount = new CustomLabelField();
		_lblAmount.setFontColor(Color.WHITE);
		_lblAmount.setMargin(22,40,0,20);
		_lblAmount.setText("Amount");		
		
		Font defaultFont = Font.getDefault();
		int fieldPadding = (defaultFont.getAdvance(_lblAmount.getText()) -
				defaultFont.getAdvance("Price") + 40);
		
		_lblPrice = new CustomLabelField();
		_lblPrice.setFontColor(Color.WHITE);
		_lblPrice.setMargin(22,fieldPadding,0,20);
		_lblPrice.setText("Price");

		_lblDuration = new CustomLabelField();
		_lblDuration.setFontColor(Color.WHITE);
		_lblDuration.setMargin(20,20,0,20);
		_lblDuration.setText("Duration");
		
		_hManagerEquity.add(_lblTicker);
		_hManagerEquity.add(_lblEquityName);
		
		_hManagerPrice.add(_lblCurrentPrice);
		_hManagerPrice.add(_lblPriceCurrent);
		
		_hManagerFunds.add(_lblFundsAvailable);
		_hManagerFunds.add(_lblAvailableFunds);
		
        final Bitmap switch_left = Bitmap.getBitmapResource("switch_left.png");
        Bitmap switch_right = Bitmap.getBitmapResource("switch_right.png");
        Bitmap switch_left_focus = Bitmap.getBitmapResource("switch_left_focus.png");
        Bitmap switch_right_focus = Bitmap.getBitmapResource("switch_right_focus.png");

        CustomLabelField _lblOrderType = new CustomLabelField();
        _lblOrderType.setFontColor(Color.WHITE);
        _lblOrderType.setMargin(24,20,0,20);
        _lblOrderType.setText("Order");
        
        _orderSwitch = new LabeledSwitch(switch_left, switch_right, 
        		switch_left_focus, switch_right_focus, "Market Order", "Limit Order", true ){
        	public void applyFont(){
        		_labelFont = getFont().derive( Font.PLAIN, getFont().getHeight() );
        	}
        };
        _orderSwitch.setMargin(20,10,0,10);
        
        CustomLabelField _lblLimitCondition = new CustomLabelField();
        _lblLimitCondition.setFontColor(Color.WHITE);
        _lblLimitCondition.setMargin(24,20,0,20);
        _lblLimitCondition.setText("Limit");
        
        _limitCondition = new LabeledSwitch(switch_left, switch_right, 
        		switch_left_focus, switch_right_focus, "Opening", "Closing", true ){
        	public void applyFont(){
        		_labelFont = getFont().derive( Font.PLAIN, getFont().getHeight() );
        	}
        };
        _limitCondition.setMargin(20,10,0,10);
        
		_txtFieldPrice = new TextField(Field.FIELD_LEADING | TextField.NO_NEWLINE);
		_txtFieldPrice.setFilter(TextFilter.get(TextFilter.NUMERIC));
		_txtFieldPrice.setMaxSize(10);
		_txtFieldPrice.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2,2,2,2)));
		_txtFieldPrice.setMargin(20,10,0,0);
		_txtFieldPrice.setPadding(10,10,10,10);
		_txtFieldPrice.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		
		_txtFieldAmount = new TextField(Field.FIELD_LEADING | TextField.NO_NEWLINE);
		_txtFieldAmount.setFilter(TextFilter.get(TextFilter.NUMERIC));
		_txtFieldAmount.setMaxSize(10);
		_txtFieldAmount.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2,2,2,2)));
		_txtFieldAmount.setMargin(20,10,0,0);
		_txtFieldAmount.setPadding(10,10,10,10);
		_txtFieldAmount.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		//probably change this to a vector
		String choices[] = {"Good Till Cancelled","Day","Immediate Or Cancel","Fill or Kill","Good Till Date"};
		_objChoiceDuration = new ObjectChoiceField("",choices){
			protected void layout(int width, int height) {
	            setMinimalWidth(width - 60);
	            super.layout(width, height);
	        };
		};
		_objChoiceDuration.setMargin(new XYEdges(20,10,0,0));
		_objChoiceDuration.setMinimalWidth(Font.getDefault().getAdvance("Good Till Cancelled"));
		
		_btnConfirm = new CustomButtonField("Confirm",Color.LIGHTGREEN);
		_btnConfirm.setChangeListener(this);
		_btnConfirm.setMargin(new XYEdges(20,0,10,0));
		
        _hManagerOrder.add(_lblOrderType);
        _hManagerOrder.add(_orderSwitch);
        
        _hManagerLimit.add(_lblLimitCondition);
        _hManagerLimit.add(_limitCondition);
        
        _hManagerPrice2.add(_lblPrice);
        _hManagerPrice2.add(_txtFieldPrice);
       
        _hManagerAmount.add(_lblAmount);
        _hManagerAmount.add(_txtFieldAmount);
        
        _hManagerDuration.add(_lblDuration);
        _hManagerDuration.add(_objChoiceDuration);
        
        _vManager.add(_hManagerEquity);
		_vManager.add(_hManagerPrice);
		_vManager.add(_hManagerFunds);
		_vManager.add(_hManagerOrder);
		_vManager.add(_hManagerLimit);
		_vManager.add(_hManagerPrice2);
		_vManager.add(_hManagerAmount);
		_vManager.add(_hManagerDuration);
		_vManager.add(_btnConfirm);
		
		add(_vManager);
	}
	public void fieldChanged(Field field, int context) {
		UiApplication diamondSec = UiApplication.getUiApplication();
		
		if(field == _btnConfirm){
			//initiate trade request...
			String URL = InfoWareConnector.API_TRADE_REQUEST_URL;
			String strDateLimit = "";
			String strLimitCondition = "";
			int TRADE_ACTION = InfoWareConnector.API_TRADE_ACTION_BUY;
			int ORDER_TYPE;
			int TIME_IN_FORCE;
			
			if(_orderSwitch.getOnState()){
				ORDER_TYPE = InfoWareConnector.API_ORDER_TYPE_MARKET;
			}else{
				ORDER_TYPE = InfoWareConnector.API_ORDER_TYPE_LIMIT;
			}
			
			if(_limitCondition.getOnState()){
				strLimitCondition = "Opening";
			}else{
				strLimitCondition  = "Closing";
			}
			//get Customer Number..
			DataContext _dbContext = new DataContext();
			String strCustomerNumber,strCSCSNumber;
			strCustomerNumber = (String)_dbContext.get("CustomerID");
			strCSCSNumber = (String)_dbContext.get("CSCS Number");
			
			switch(_objChoiceDuration.getSelectedIndex()){
				case 1:
					TIME_IN_FORCE = InfoWareConnector.API_TIME_IN_FORCE_GOODTILLCANCELLED;
					break;
				case 2:
					TIME_IN_FORCE = InfoWareConnector.API_TIME_IN_FORCE_DAY;
					break;
				case 3:
					TIME_IN_FORCE = InfoWareConnector.API_TIME_IN_FORCE_IMMEDIATEORCANCEL;
					break;
				case 4:
					TIME_IN_FORCE = InfoWareConnector.API_TIME_IN_FORCE_FILLORKILL;
					break;
				case 5:
					TIME_IN_FORCE = InfoWareConnector.API_TIME_IN_FORCE_GOODTILLDATE;
					break;
				default:
					TIME_IN_FORCE = 0;
					break;
			}
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			strDateLimit =  dateFormat.format(date);
			//build endpoint URL..
			URL = URL + strCustomerNumber + "|"+strCSCSNumber +"|"+_theStock._ticker+
			"|"+TRADE_ACTION+"|"+_txtFieldAmount.getText()+"|"+ORDER_TYPE+"|"+_txtFieldPrice.getText()+
			"|"+TIME_IN_FORCE+"|"+strDateLimit;
			TradingBuyConnector _connector = new TradingBuyConnector(URL);
			_connector.start();
			
			Trade _theTrade = new Trade();
			
			if(_orderSwitch.getOnState()){
			_theTrade.strOrderType = "Market";
			}else{_theTrade.strOrderType = "Limit";}
			
			_theTrade.strLimitCondition = strLimitCondition;
			_theTrade.strAmount = _txtFieldAmount.getText();
			_theTrade.strDuration = (String)_objChoiceDuration.getChoice(_objChoiceDuration.getSelectedIndex());;
			
			diamondSec.pushScreen(new TradingConfirmationScreen(_theStock, _theTrade));
		}
	}

}
