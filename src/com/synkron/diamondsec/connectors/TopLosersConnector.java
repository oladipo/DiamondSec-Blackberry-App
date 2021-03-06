package com.synkron.diamondsec.connectors;

import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.samples.toolkit.ui.component.ListStyleButtonField;
import com.synkron.diamondsec.LoginStatusScreen;
import com.synkron.diamondsec.MarketInfoScreen;

public class TopLosersConnector  extends InfoWareConnector{

	public TopLosersConnector(String _Url) {
		
		super(_Url);
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
            		theScreen._topLoosers.deleteAll();
            		
            		try{
						JSONObject obj = new JSONObject(_Result);
						JSONArray jsonArray = obj.getJSONArray("Rows");
						int backColor = Color.DARKBLUE;
						String sb = "";
						
						for(int i = 0; i < jsonArray.length(); i++){
							JSONArray inner  = jsonArray.getJSONArray(i);
							
							for(int j = 0; j < inner.length() ; j++){
								JSONObject innerObj = inner.getJSONObject(j);
								if(i % 2 == 0){
									backColor = Color.CORNFLOWERBLUE;
								}else{
									backColor = Color.DARKBLUE;
								}
								
								if(j == 0){
									//Ticker
									sb = "["+innerObj.getString("Value")+"]";
								}
								
								if(j == 1){
									//Ticker
									sb = sb + " ["+innerObj.getString("Value")+"] \n";
								}
								
								if(j == 4){
									//Ticker
									sb = sb + "Price: ["+innerObj.getString("Value")+"]";
								}
								
								if(j == 6){
									//Ticker
									sb = sb + " Change: ["+innerObj.getString("Value")+"]";
								}
							}
							
							
							ListStyleButtonField lsBtnField = new ListStyleButtonField(sb,Field.USE_ALL_WIDTH ){
								int color = Color.WHITE;
								
								protected void paint(Graphics graphics) {
										graphics.setColor(color);
										super.paint(graphics);
						        }
							};
							
							lsBtnField.setMargin(new XYEdges(0,20,0,20));
							lsBtnField.setBackground(BackgroundFactory.createSolidBackground(backColor));
			
							theScreen._topLoosers.add(lsBtnField);
							
							sb = "";
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
