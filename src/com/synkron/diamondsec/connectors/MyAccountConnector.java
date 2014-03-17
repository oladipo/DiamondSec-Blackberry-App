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
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.synkron.diamondsec.LoginStatusScreen;
import com.synkron.diamondsec.SummaryScreen;
public class MyAccountConnector extends InfoWareConnector{

	public MyAccountConnector(String Url) {
		super(Url);
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
			            		SummaryScreen theScreen = (SummaryScreen)UiApplication.
								getUiApplication().getActiveScreen();
			            		
			            		try{
			            			GridFieldManager _grdSummary = new GridFieldManager(5,2,Manager.USE_ALL_HEIGHT);
			            			_grdSummary.setColumnPadding(20);
			            			_grdSummary.setRowPadding(15);
			            			_grdSummary.setMargin(10, 50, 20, 50);
			            			_grdSummary.setBackground(BackgroundFactory.createSolidBackground(Color.LIGHTBLUE));
			            			
			            			GridFieldManager _grdSubSummary = new GridFieldManager(2,2,Field.USE_ALL_WIDTH); 
			            			_grdSubSummary.setColumnPadding(20);
			            			_grdSubSummary.setRowPadding(20);
			            			_grdSubSummary.setMargin(10, 50, 20, 50);
			            			_grdSubSummary.setBackground(BackgroundFactory.createSolidBackground(Color.DARKBLUE));
			            			
									JSONObject obj = new JSONObject(_Result);
									JSONArray jsonArray = obj.getJSONArray("Rows");

									for(int i = 0; i < jsonArray.length(); i++){
										//add a field to the screen..
										JSONArray inner  = jsonArray.getJSONArray(i);
										
										for(int j = 0; j < inner.length() ; j++){
											JSONObject innerObj = inner.getJSONObject(j);
											
											System.out.println(innerObj.getString("Value"));
											if(j == 8){
												_grdSummary.add(new LabelField("Real Estate", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField(""){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField("Portfolio Value", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField(innerObj.getString("Value")){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
											}
											if(j == 7){
												_grdSummary.add(new LabelField("Cash Balance", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField(innerObj.getString("Value"),Field.FIELD_RIGHT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField("Equities", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField(""){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
											}
											if(j == 10){
												_grdSubSummary.add(new LabelField("Funds Available for Trading", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSubSummary.add(new LabelField(""){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												
												_grdSubSummary.add(new LabelField("Margin Allowed", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSubSummary.add(new LabelField(innerObj.getString("Value")){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
											}
											if(j == 11){
												_grdSummary.add(new LabelField("Fixed Income", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField(innerObj.getString("Value")){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
											}
										}
										//previously stored, checked storage expiration to refresh data store.
										
										/*DataContext _dContext = new DataContext();
										_dContext.set("isSaved", "false");
										_dContext.commit();*/
										theScreen._summary.deleteAll();
										theScreen._summary.add(_grdSummary);
										theScreen._summary.add(_grdSubSummary);
									}
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
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
