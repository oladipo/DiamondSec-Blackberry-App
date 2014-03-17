package com.synkron.diamondsec.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.samples.toolkit.ui.component.ListStyleButtonField;
import com.synkron.diamondsec.LoginStatusScreen;
import com.synkron.diamondsec.OrderHistoryScreen;
import com.synkron.diamondsec.TradingScreen;
import com.synkron.diamondsec.entities.Stock;
import com.synkron.diamondsec.utils.SplitString;

public class OrderHistoryConnector extends InfoWareConnector{

	public OrderHistoryConnector(String Url) {
		super(Url);
		// TODO Auto-generated constructor stub
	}

	public void run(){
		synchronized(UiApplication.getEventLock()){
			UiApplication.getUiApplication().pushScreen(new LoginStatusScreen());
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
			            	
			            	public void run(){
			            		OrderHistoryScreen theScreen = (OrderHistoryScreen)UiApplication.getUiApplication().getActiveScreen();
			            		theScreen._allHistory.deleteAll();
			            		
			            		try{
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
											if(j ==0){
												//TxnDate
												sb = sb + "Transaction Date:"+innerObj.getString("Value");
											}
											if(j==1){
												//EffectiveDate
												sb = sb + "|\nEffective Date:"+innerObj.getString("Value");
											}
											if(j==2){
												//Description
												sb = sb + "|\n"+innerObj.getString("Value");
											}
											if(j==3){
												//Amount
												sb = sb + "|Amount:"+innerObj.getString("Value");
											}
											
										}
										String strDisplay = sb.replace('|', ' ');
										//previously stored, checked storage expiration to refresh data store.
										
										/*DataContext _dContext = new DataContext();
										_dContext.set("isSaved", "false");
										_dContext.commit();*/
									    
										ListStyleButtonField lsBtnField = new ListStyleButtonField(strDisplay,Field.USE_ALL_WIDTH ){
											int color = Color.WHITE;
											
											protected void paint(Graphics graphics) {
													graphics.setColor(color);
													super.paint(graphics);
									        }
										};
										
										lsBtnField.setCookie(sb);

										lsBtnField.setMargin(new XYEdges(0,20,0,20));
										lsBtnField.setBackground(BackgroundFactory.createSolidBackground(backColor));
						
										theScreen._allHistory.add(lsBtnField);
										
										sb = "";
									}
			            		}catch(JSONException e){
			            			e.printStackTrace();
			            		}
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
		        				//UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
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
		        				//UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
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
		    				//UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		    					UiApplication.getUiApplication().pushScreen(
		    							new LoginStatusScreen(ioe.getMessage()));
		    		    }
		            }
				}
		}
	}
}
