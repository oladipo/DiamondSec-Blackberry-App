package com.synkron.diamondsec;

import java.util.Date;

import com.samples.toolkit.ui.component.ListStyleButtonField;
import com.synkron.diamondsec.connectors.*;
import com.synkron.diamondsec.entities.Stock;
import com.synkron.diamondsec.utils.DataContext;
import com.synkron.diamondsec.utils.MarketDatabase;
import com.synkron.diamondsec.utils.SplitString;

import net.rim.device.api.database.Cursor;
import net.rim.device.api.database.DataTypeException;
import net.rim.device.api.database.DatabaseException;
import net.rim.device.api.database.Row;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;

import net.rim.device.api.ui.UiApplication;
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
	public VerticalFieldManager _vManager;
	
	public StockListScreen() {
		super();
		_hManager.add(new CustomButtonField("Stock List", Color.DARKBLUE));
		
		super.setupStatusCommands();
		_btnStocksList.setFocus();
		_hSearchManager = new HorizontalFieldManager();
		_vManager = new VerticalFieldManager();
		
		_lblSearch = new LabelField("Search");
		_lblSearch.setMargin(30,5,0,10);
		
		_txtSearch = new EditField(Field.FIELD_LEADING | TextField.NO_NEWLINE);
		_txtSearch.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2,2,2,2)));
		_txtSearch.setMargin(20,20,20,0);
		_txtSearch.setPadding(10,10,10,10);
		_txtSearch.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		
		_btnSearch = new CustomButtonField("Search",Color.GREEN);
		_btnSearch.setMargin(13,5,0,0);
		_btnSearch.setPadding(10,10,10,10);
		_btnSearch.setChangeListener(new FieldChangeListener(){

			public void fieldChanged(Field field, int context) {
				String param = _txtSearch.getText();
				MarketDatabase _myDB = new MarketDatabase();
				Cursor myCursor = _myDB.Find(param);
				Row myRow;
				int i = 0;
				int backColor = Color.DARKBLUE;
				_vManager.deleteAll();
				
					try {
						while(myCursor.next()){
								myRow = myCursor.getRow();
								i++;
								if(i % 2 == 0){
									backColor = Color.LIGHTBLUE;
								}else{
									backColor = Color.DARKBLUE;
								}
								ListStyleButtonField lsBtnField = new ListStyleButtonField(myRow.getString(0)+" "+myRow.getString(1)+"\n"+myRow.getString(2)+myRow.getString(3),0){
								int color = Color.WHITE;
								protected void paint(Graphics graphics) {
										graphics.setColor(color);
										super.paint(graphics);
							    }
							};
							lsBtnField.setCookie(myRow.getString(0)+"|"+myRow.getString(1)+"|\n"+myRow.getString(2)+"|"+myRow.getString(3));
							
							lsBtnField.setChangeListener(new FieldChangeListener(){
								public void fieldChanged(Field field, int context) {
									ListStyleButtonField _myButton = (ListStyleButtonField)field;
									String _txtStockCode[] = SplitString.split((_myButton.getCookie().toString()),"|");
									Stock theStock = new Stock();
									theStock._ticker = _txtStockCode[0];
									theStock._name = _txtStockCode[1];
									//replace the new line with empty string...
									theStock._currentPrice = _txtStockCode[2].replace('\n', ' ').substring(8);
									UiApplication.getUiApplication().pushScreen(new TradingScreen(theStock));
								}
							});

							lsBtnField.setMargin(new XYEdges(0,20,0,20));
							lsBtnField.setBackground(BackgroundFactory.createSolidBackground(backColor));
							_vManager.add(lsBtnField);
						}
						if(i == 0){
							//No Data to display...
							_vManager.add(new LabelField("No Records Found..."));
						}
						
						_myDB.Close();
						_myDB._selectStatement.close();
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (DataTypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		
		//_hSearchManager.add(_lblSearch);
		_hSearchManager.add(_btnSearch);
		_hSearchManager.add(_txtSearch);
		add(_hSearchManager);
		getStockList();
		add(_vManager);
	}

	private void getStockList(){
		String saved = "false";
		Date now = new Date();
		String saveDate, getDate;
		long lapseTime = 0;
		
		DataContext _dContext = new DataContext();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		
		if(_dContext.get("isSaved") != null){
			saved = _dContext.get("isSaved").toString();
		}

		if(_dContext.get("savedDate") != null){
			saveDate = _dContext.get("savedDate").toString();
			getDate = formatter.format(now);
			
			long parseGet = HttpDateParser.parse(getDate);
			long parseSave =  HttpDateParser.parse(saveDate);
			lapseTime = (parseGet - parseSave);
		}
		//24 hours = 86400000 milliseconds
		if(saved.equals("false") || (lapseTime > 86400000)){
			String Url = InfoWareConnector.API_FULL_PRICE_LIST_URL;
			//String Url = InfoWareConnector.API_TRADEABLE_STOCKS_URL;
			StockListConnector _connector = new StockListConnector(Url);
			//_connector.run();
			_connector.start();
		}
		else{
			//retrieve price list from data store.
			//data store will need to be refreshed periodically...
			_vManager.deleteAll();
			MarketDatabase _myDB = new MarketDatabase();
			Cursor myCursor = _myDB.SelectRecords();
			Row myRow;
			int i = 0;
			int backColor = Color.DARKBLUE;
			
				try {
					while(myCursor.next()){
							myRow = myCursor.getRow();
							i++;
							if(i % 2 == 0){
								backColor = Color.LIGHTBLUE;
							}else{
								backColor = Color.DARKBLUE;
							}
							ListStyleButtonField lsBtnField = new ListStyleButtonField(myRow.getString(0)+" "+myRow.getString(1)+"\n"+myRow.getString(2)+myRow.getString(3),0){
							int color = Color.WHITE;
							protected void paint(Graphics graphics) {
									graphics.setColor(color);
									super.paint(graphics);
						    }
						};
						lsBtnField.setCookie(myRow.getString(0)+"|"+myRow.getString(1)+"|\n"+myRow.getString(2)+"|"+myRow.getString(3));
						
						lsBtnField.setChangeListener(new FieldChangeListener(){
							public void fieldChanged(Field field, int context) {
								ListStyleButtonField _myButton = (ListStyleButtonField)field;
								String _txtStockCode[] = SplitString.split((_myButton.getCookie().toString()),"|");
								Stock theStock = new Stock();
								theStock._ticker = _txtStockCode[0];
								theStock._name = _txtStockCode[1];
								//replace the new line with empty string...
								theStock._currentPrice = _txtStockCode[2].replace('\n', ' ').substring(8);
								UiApplication.getUiApplication().pushScreen(new TradingScreen(theStock));
							}
						});

						lsBtnField.setMargin(new XYEdges(0,20,0,20));
						lsBtnField.setBackground(BackgroundFactory.createSolidBackground(backColor));
						_vManager.add(lsBtnField);
					}
					if(i == 0){
						//No Data to display...
					}
					
					_myDB.Close();
					_myDB._selectStatement.close();
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (DataTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
