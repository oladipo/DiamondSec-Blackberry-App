package com.synkron.diamondsec.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.samples.toolkit.ui.component.ListStyleButtonField;
import com.synkron.diamondsec.LoginStatusScreen;
import com.synkron.diamondsec.StockListScreen;
import com.synkron.diamondsec.TradingScreen;
import com.synkron.diamondsec.entities.Stock;
import com.synkron.diamondsec.utils.DataContext;
import com.synkron.diamondsec.utils.MarketDatabase;
import com.synkron.diamondsec.utils.SplitString;

public class StockListConnector extends InfoWareConnector{

	public StockListConnector(String Url) {
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
            
            //System.out.print(_Result);
            synchronized(UiApplication.getEventLock()){
            	UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
            }
            
    		UiApplication.getUiApplication().invokeLater(new Runnable(){

    			public void run() {
    				
    				StockListScreen theScreen = (StockListScreen)UiApplication.
    												getUiApplication().getActiveScreen();
    				MarketDatabase _myDb = new MarketDatabase();
    				_myDb.DeleteStocks();
    				theScreen._vManager.deleteAll();
    				try {
						JSONObject obj = new JSONObject(_Result);
						JSONArray jsonArray = obj.getJSONArray("Rows");
						
						String sb = "";
						int backColor = Color.DARKBLUE;
						
						
						for(int i = 0; i < jsonArray.length(); i++){
							//add a field to the screen..
							JSONArray inner  = jsonArray.getJSONArray(i);
								
							if(i % 2 == 0){
								backColor = Color.LIGHTBLUE;
							}else{
								backColor = Color.DARKBLUE;
							}
							for(int j = 0; j < inner.length() ; j++){
								JSONObject innerObj = inner.getJSONObject(j);
								
								System.out.println(innerObj.getString("Value"));
								//add a new line
								if(j == 2){
									sb = sb + "|\n";
								}
								//add a label for price
								if(j == 3){
									sb = sb + "Price: "+ innerObj.getString("Value")+ "|";
								}
								if(j == 5){ 
									sb = sb + " Change: "+ innerObj.getString("Value")+ "|";
								}
								if(j == 1){
									sb = sb + "|"+ innerObj.getString("Value");
								}
								if(j == 0){
									sb = sb + innerObj.getString("Value");
								}
							}
							String strDisplay = sb.replace('|', ' ');

							String _txtStockCode[] = SplitString.split(sb,"|");
							_myDb.InsertStock(_txtStockCode[0], _txtStockCode[1], _txtStockCode[2].trim(), _txtStockCode[3]);
							
							ListStyleButtonField lsBtnField = new ListStyleButtonField(strDisplay,0){
								int color = Color.WHITE;
								
								protected void paint(Graphics graphics) {
										graphics.setColor(color);
										super.paint(graphics);
						        }
							};
							
							lsBtnField.setCookie(sb);
							
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
							theScreen._vManager.add(lsBtnField);
							
							sb = "";
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					_myDb.Close();
					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
					
					DataContext _dContext = new DataContext();
					_dContext.set("isSaved", "true");
					_dContext.set("savedDate", formatter.format(date));
					_dContext.commit();
					
    			}
    		});
            
		}catch(Exception Ex){
			 _Result = "ERROR fetching content: " + Ex.toString();
			 
			 System.out.print(Ex.getMessage());
				synchronized(UiApplication.getEventLock()){
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
						UiApplication.getUiApplication().pushScreen(
								new LoginStatusScreen(Ex.getMessage()));
			    }
		}
		finally
		{
			// Close OutputStream.
            if(oStream != null)
            {
                try
                {
                	oStream.close();
                }
                catch(IOException e)
                {
                	 System.out.print("ERROR fetching content: " + e.getMessage());
     				synchronized(UiApplication.getEventLock()){
    					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
    						UiApplication.getUiApplication().pushScreen(
    								new LoginStatusScreen(e.getMessage()));
    			    }
                }
            }

            // Close InputStream.
            if(iStream != null)
            {
                try
                {
                	iStream.close();
                }
                catch(IOException e)
                {
                	System.out.print("ERROR fetching content: " + e.getMessage());
    				synchronized(UiApplication.getEventLock()){
    					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
    						UiApplication.getUiApplication().pushScreen(
    								new LoginStatusScreen(e.getMessage()));
    			    }
    				
                }
            }
            // Close Connection.
            try
            {
                _connection.close();
            }
            catch(IOException ioe)
            {
            	System.out.print("ERROR fetching content: " + ioe.getMessage());
				synchronized(UiApplication.getEventLock()){
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
						UiApplication.getUiApplication().pushScreen(
								new LoginStatusScreen(ioe.getMessage()));
			    }
            }
		}
		
	}
}
