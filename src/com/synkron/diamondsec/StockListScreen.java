package com.synkron.diamondsec;

import com.synkron.diamondsec.connectors.*;
import com.synkron.diamondsec.utils.DataContext;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;

public class StockListScreen extends SubScreen {
	LabelField _lblSearch;
	EditField _txtSearch;
	CustomButtonField _btnSearch;
	
	HorizontalFieldManager _hSearchManager;
	public RichTextField _rtfOutput;
	
	public StockListScreen() {
		super();
		_hManager.add(new CustomButtonField("Stock List", Color.DARKBLUE));
		
		super.setupStatusCommands();
		_btnStocksList.setFocus();
		_hSearchManager = new HorizontalFieldManager(Field.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		
		_lblSearch = new LabelField("Search");
		_lblSearch.setMargin(30,5,0,10);
		
		_txtSearch = new EditField(Field.FIELD_LEADING | TextField.NO_NEWLINE | USE_ALL_WIDTH);
		_txtSearch.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2,2,2,2)));
		_txtSearch.setMargin(20,5,0,0);
		_txtSearch.setPadding(10,10,10,10);
		_txtSearch.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		
		_btnSearch = new CustomButtonField("Go",Color.GREEN);
		_btnSearch.setMargin(20,20,0,0);
		_btnSearch.setPadding(10,10,10,10);
		
		_hSearchManager.add(_lblSearch);
		_hSearchManager.add(_txtSearch);
		_hSearchManager.add(_btnSearch);
		
		_rtfOutput = new RichTextField();
		_rtfOutput.setText("Retrieving data. Please wait...");

		add(_hSearchManager);

		add(_rtfOutput);
		
		getStockList();
	}

	private void getStockList(){
		String saved = "false";
		DataContext _dContext = new DataContext();
		if(_dContext.get("isSaved") != null){
			saved = _dContext.get("isSaved").toString();
		}

		if(!saved.equals("true")){
			String Url = InfoWareConnector.API_FULL_PRICE_LIST_URL;
			StockListConnector _connector = new StockListConnector(Url+";deviceside=true");
			//_connector.run();
			_connector.start();
		}
		else{
			//retrieve price list from data store.
			//data store will need to be refreshed periodically...
			
		}
	}
}
