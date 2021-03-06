package com.synkron.diamondsec.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.synkron.diamondsec.CustomLabelField;
import com.synkron.diamondsec.LoginStatusScreen;
import com.synkron.diamondsec.SummaryScreen;
import com.synkron.diamondsec.utils.DataContext;
import com.synkron.diamondsec.utils.NumberFormatter;
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
			            		Font myFont = Font.getDefault().derive(Font.PLAIN,6, Ui.UNITS_pt);
			            		try{
			            			HorizontalFieldManager _hUserProfile = new HorizontalFieldManager(Manager.USE_ALL_WIDTH);
			            			_hUserProfile.setMargin(10, 20, 10, 50);
			            			
			            			GridFieldManager _grdSummary = new GridFieldManager(5,2,Manager.USE_ALL_HEIGHT);
			            			_grdSummary.setColumnPadding(20);
			            			_grdSummary.setRowPadding(15);
			            			_grdSummary.setMargin(5, 50, 10, 50);
			            			_grdSummary.setBackground(BackgroundFactory.createSolidBackground(Color.LIGHTBLUE));
			            			
			            			GridFieldManager _grdSubSummary = new GridFieldManager(2,2,Field.USE_ALL_WIDTH); 
			            			_grdSubSummary.setColumnPadding(10);
			            			_grdSubSummary.setRowPadding(20);
			            			_grdSubSummary.setMargin(5, 50, 0, 50);
			            			_grdSubSummary.setBackground(BackgroundFactory.createSolidBackground(Color.DARKBLUE));
			            			
			            			GridFieldManager _grdTotal = new GridFieldManager(2,2,Field.USE_ALL_WIDTH); 
			            			_grdTotal.setColumnPadding(20);
			            			_grdTotal.setRowPadding(15);
			            			_grdTotal.setMargin(0, 50, 20, 50);
			            			_grdTotal.setBackground(BackgroundFactory.createSolidBackground(Color.DARKBLUE));
			            			
									JSONObject obj = new JSONObject(_Result);
									JSONArray jsonArray = obj.getJSONArray("Rows");

				            		double _total = 0; 
				            		double _fundsAvailable = 0;
				            		String customerName = null;
				            		String phone =  null;
				            		
									for(int i = 0; i < jsonArray.length(); i++){
										//add a field to the screen..
										JSONArray inner  = jsonArray.getJSONArray(i);
										
										for(int j = 0; j < inner.length() ; j++){
											JSONObject innerObj = inner.getJSONObject(j);
											
											System.out.println(innerObj.getString("Value"));
											
											if(j == 1){
												customerName = innerObj.getString("Value");
											}
											if(j == 3){
												phone = innerObj.getString("Value");
											}
											if(j == 6){
												_fundsAvailable += innerObj.getDouble("Value");
											}
											if(j == 7){
												_grdSummary.add(new LabelField("Cash Balance", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdSummary.add(new LabelField(NumberFormatter.formatNumber(innerObj.getDouble("Value"),2,","),Field.FIELD_RIGHT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												
												_total = _total + innerObj.getDouble("Value");
												_fundsAvailable += innerObj.getDouble("Value");
											}
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
												_grdSummary.add(new LabelField("Equities", Field.FIELD_LEFT){
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
												
												_total = _total + innerObj.getDouble("Value");
												
												/*_grdSummary.add(new LabelField("Portfolio Value", Field.FIELD_LEFT){
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
												});*/
											}
											if(j == 9){
												_fundsAvailable += innerObj.getDouble("Value");
											}
											if(j == 10){
												_fundsAvailable += innerObj.getDouble("Value");
												
												_grdSubSummary.add(new LabelField("Funds Available for Trading", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
													
													protected void layout(int width, int height) {
												        super.layout(width, height);
												        this.setExtent(Display.getWidth()/3, this.getHeight());
												    }
												    public int getPreferredWidth() {
												    	return Display.getWidth()/3;
												    }
												    
												    public int getPreferredHeight() {
												    	return this.getHeight();
												    }
												});
												_grdSubSummary.add(new LabelField(NumberFormatter.formatNumber(_fundsAvailable,2,",")){
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
												_grdSubSummary.add(new LabelField(innerObj.getString("Value"),Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												
												_total = _total + innerObj.getDouble("Value");
												
												_grdTotal.add(new LabelField("Total ", Field.FIELD_LEFT){
													public void paint(Graphics graphics) {
											            graphics.setColor(Color.WHITE);
											            super.paint(graphics);  
											        }
												});
												_grdTotal.add(new LabelField(NumberFormatter.formatNumber(_total,2,","),Field.FIELD_LEFT){
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
												_total = _total + innerObj.getDouble("Value");
											}
										}
										//previously stored, checked storage expiration to refresh data store.
										
										DataContext _dContext = new DataContext();
										_dContext.set("funds", String.valueOf(_fundsAvailable));
										_dContext.commit();
										
										_dContext.set("customerName", customerName);
										_dContext.commit();
										
										_dContext.set("phone", phone);
										_dContext.commit();
										
										theScreen._summary.deleteAll();
										
										
										CustomLabelField lblCustomerProfile = new CustomLabelField();
										lblCustomerProfile.setText(customerName+" "+_dContext.get("CustomerID")+" "+_dContext.get("CSCS Number"));
										//lblCustomerProfile.setMargin(10,5,0,10);
										lblCustomerProfile.setFontColor(Color.WHITE);
										lblCustomerProfile.setFont(myFont);

										_hUserProfile.add(lblCustomerProfile);
										
										theScreen._summary.add(_hUserProfile);
										theScreen._summary.add(_grdSummary);
										theScreen._summary.add(_grdSubSummary);
										theScreen._summary.add(_grdTotal);
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
