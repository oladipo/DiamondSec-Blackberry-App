package com.synkron.diamondsec;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.util.StringProvider;

public class SubScreen extends BaseScreen {
	HorizontalFieldManager _hStatusManager, _hManager;
	CustomButtonField _btnMyAccount, _btnStocksList, 
		_btnMarketInfo, _btnOrderSummary,_btnLogOff;
	
	public SubScreen() {
		
		addMenuItem(new MenuItem(new StringProvider("Logout"), 0, 0){
			public void run(){
				UiApplication diamondSec = UiApplication.getUiApplication();
				diamondSec.pushScreen(new LoginScreen());
			}
		});
		_btnLogOff = new CustomButtonField("Log Off", Color.DARKBLUE);
		_btnLogOff.setChangeListener(new FieldChangeListener(){

			public void fieldChanged(Field field, int context) {
				UiApplication diamondSec = UiApplication.getUiApplication();
					diamondSec.pushScreen(new LoginScreen());
			}
			
		});
		
    	_hManager = new HorizontalFieldManager(USE_ALL_WIDTH | Manager.FIELD_VCENTER);
    	Background bg = BackgroundFactory.createSolidBackground(Color.BLACK);
    	_hManager.setBackground(bg);
    	
    	_hManager.add(_btnLogOff);
    	
    	setTitle(_hManager);
	}
	
	SubScreen(long style){
	
	}
	
	public void setTitle(Field title){
    	//add navigation buttons here..
    	super.setTitle(title);
	}
	
	public void setupStatusCommands() {
		
    	HorizontalFieldManager _hStatusManager = new HorizontalFieldManager(USE_ALL_WIDTH | Manager.FIELD_VCENTER
    			| Manager.HORIZONTAL_SCROLL);
    	Background bg = BackgroundFactory.createSolidBackground(Color.BLACK);
    	_hStatusManager.setBackground(bg);
		
		_btnMyAccount = new CustomButtonField("My Account", Color.DARKBLUE);
		_btnMyAccount.setChangeListener(new FieldChangeListener(){

			public void fieldChanged(Field field, int context) {
				UiApplication diamondSec = UiApplication.getUiApplication();
					diamondSec.pushScreen(new SummaryScreen());
			}
			
		});
		
		_btnStocksList = new CustomButtonField("Stocks List", Color.DARKBLUE);
		_btnStocksList.setChangeListener(new FieldChangeListener(){

			public void fieldChanged(Field field, int context) {
				UiApplication diamondSec = UiApplication.getUiApplication();
					diamondSec.pushScreen(new StockListScreen());
			}
			
		});
		
		_btnMarketInfo = new CustomButtonField("Market Info", Color.DARKBLUE);
		_btnMarketInfo.setChangeListener(new FieldChangeListener(){

			public void fieldChanged(Field field, int context) {
				UiApplication diamondSec = UiApplication.getUiApplication();
					diamondSec.pushScreen(new MarketInfoScreen());
			}
			
		});
		_btnOrderSummary = new CustomButtonField("Order History", Color.DARKBLUE);
		_btnOrderSummary.setChangeListener(new FieldChangeListener(){

			public void fieldChanged(Field field, int context) {
				UiApplication diamondSec = UiApplication.getUiApplication();
					diamondSec.pushScreen(new OrderHistoryScreen());
			}
			
		});
		
			_hStatusManager.add(_btnMyAccount);
			_hStatusManager.add(_btnStocksList);
			_hStatusManager.add(_btnMarketInfo);
			_hStatusManager.add(_btnOrderSummary);
			
			setStatus(_hStatusManager);
	}
    
	public void close(){
		UiApplication.getUiApplication().popScreen(this);
    }
	
	protected boolean onSavePrompt(){
		return true;
		
	}
}
