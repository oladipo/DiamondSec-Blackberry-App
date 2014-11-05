package com.synkron.diamondsec.connectors;

import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.synkron.diamondsec.LoginStatusScreen;
import com.synkron.diamondsec.MarketInfoScreen;
import com.synkron.diamondsec.utils.NumberFormatter;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class ASIMarketInfoConnector extends InfoWareConnector{

	public ASIMarketInfoConnector(String Url) {
		super(Url);
		// TODO Auto-generated constructor stub
	}

	public void run(){
		
		synchronized(UiApplication.getEventLock()){
			UiApplication.getUiApplication().pushScreen(new LoginStatusScreen());
		}
		
		OutputStream oStream = null;
		InputStream iStream = null;
		
		try{
			_descriptor = _factory.getConnection(_Url);
			if(_descriptor != null){
				 _connection = _descriptor.getConnection();
			}
			
			OutputConnection outConnection = (OutputConnection) _connection;
			oStream = outConnection.openOutputStream();
			String getCommand = "GET " + "/" + " HTTP/1.0\r\n\r\n";
			oStream.write(getCommand.getBytes());
			oStream.flush();
            
			InputConnection inputConn = (InputConnection) _connection;
			iStream = inputConn.openInputStream();
            byte[] data = net.rim.device.api.io.IOUtilities.streamToBytes(iStream);
            _Result = new String(data);
            System.out.println(_Result);
            
    		
            synchronized(UiApplication.getEventLock()){
	            	UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
	        }
            UiApplication.getUiApplication().invokeLater(new Runnable(){
            	public void run(){
            		MarketInfoScreen theScreen = (MarketInfoScreen)UiApplication.getUiApplication().getActiveScreen();
            		
            		try{
						JSONObject obj = new JSONObject(_Result);
						JSONArray jsonArray = obj.getJSONArray("Rows");
            			
            			GridFieldManager _grdAllShareIndex = new GridFieldManager(7,2,Manager.USE_ALL_HEIGHT | Manager.USE_ALL_WIDTH);
            			_grdAllShareIndex.setColumnPadding(20);
            			_grdAllShareIndex.setRowPadding(15);
            			_grdAllShareIndex.setMargin(5, 10, 10, 10);
            			_grdAllShareIndex.setBackground(BackgroundFactory.createSolidBackground(Color.CORNFLOWERBLUE));
            			
						for(int i = 0; i < jsonArray.length(); i++){
							JSONArray inner  = jsonArray.getJSONArray(i);
							
							for(int j = 0; j < inner.length() ; j++){
								JSONObject innerObj = inner.getJSONObject(j);
								if(j == 0){
									_grdAllShareIndex.add(new LabelField("Current NSE ASI", Field.FIELD_LEFT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
									_grdAllShareIndex.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
										
										public void setFont(Font font){
											Font myFont = Font.getDefault().derive(Font.PLAIN,6, Ui.UNITS_pt);
											super.setFont(myFont);
										}
									});
								}
								if(j == 1){
									_grdAllShareIndex.add(new LabelField("ASI Year To Date Change", Field.FIELD_LEFT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
									_grdAllShareIndex.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
								}
								if(j == 2){
									_grdAllShareIndex.add(new LabelField("ASI Month To Date Change", Field.FIELD_LEFT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
									_grdAllShareIndex.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
								}
								if(j == 3){
									_grdAllShareIndex.add(new LabelField("ASI Week To Date Change", Field.FIELD_LEFT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
									_grdAllShareIndex.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
								}
								if(j == 4){
									_grdAllShareIndex.add(new LabelField("Market Capitalization", Field.FIELD_LEFT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
									_grdAllShareIndex.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT | Field.USE_ALL_WIDTH){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
								}
								if(j == 5){
									_grdAllShareIndex.add(new LabelField("Traded Volume", Field.FIELD_LEFT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
									_grdAllShareIndex.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
								}
								if(j == 6){
									_grdAllShareIndex.add(new LabelField("Traded Value", Field.FIELD_LEFT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
									_grdAllShareIndex.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT){
										public void paint(Graphics graphics) {
								            graphics.setColor(Color.WHITE);
								            super.paint(graphics);  
								        }
									});
								}
							}
							theScreen._allShareIndex.deleteAll();
							
							theScreen._allShareIndex.add(_grdAllShareIndex);
						}
            		}catch(JSONException ex){
            			ex.printStackTrace();
            		}
            }});
		}
		catch(Exception ex){
	
		}
	}
}
