package com.synkron.diamondsec.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import net.rim.device.api.ui.UiApplication;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.synkron.diamondsec.LoginStatusScreen;
import com.synkron.diamondsec.SummaryScreen;
import com.synkron.diamondsec.utils.DataContext;


public class LoginConnector extends InfoWareConnector{
    String strCustomerID;
    
	public LoginConnector(String Url, String CustomerID) {
		super(Url);
		strCustomerID = CustomerID;
	}
	
	public void run(){
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
		            
		    		UiApplication.getUiApplication().invokeLater(new Runnable(){
		    			
						public void run() {
							try {
								JSONObject obj = new JSONObject(_Result);
								JSONArray jsonArray = obj.getJSONArray("Rows");
								
								String sb = "";
								
								for(int i = 0; i < jsonArray.length(); i++){
									//add a field to the screen..
									JSONArray inner  = jsonArray.getJSONArray(i);
									for(int j = 0; j < inner.length() ; j++){
										JSONObject innerObj = inner.getJSONObject(j);
										
										System.out.println(innerObj.getString("Value"));
										
										sb = sb + " "+ innerObj.getString("Value");
									}
								}
								sb = sb.trim();
								sb = sb.toLowerCase();
								
								if(sb.equals("true")){
									//Save Customer Id to Persistent Store Instance..
									DataContext _dContext = new DataContext();
									_dContext.set("CustomerID", strCustomerID);
									
									_dContext.commit();
									
									UiApplication.getUiApplication().invokeLater(new CSCSConnector(InfoWareConnector.API_GET_CUSTOMER_CSCS_NUMBERS_URL+strCustomerID));
									//pop the loading screen indicator
								    UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
									//TODO Load Customer Account Information for Summary Screen..
								    UiApplication.getUiApplication().pushScreen(
											new SummaryScreen()
									);
								
								}else{
									UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
									UiApplication.getUiApplication().pushScreen(
											new LoginStatusScreen("Login Failed \n Invalid Username or Password ")
									);
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
								UiApplication.getUiApplication().pushScreen(
										new LoginStatusScreen(e.getMessage())
								);
							}
		    			}
		
		    		});
		} 
		catch (IOException e) {
			e.printStackTrace();
			
			synchronized(UiApplication.getEventLock()){
				UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
					UiApplication.getUiApplication().pushScreen(
							new LoginStatusScreen(e.getMessage()));
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
                catch(Exception e){
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
                catch(Exception e){
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
            catch(Exception e){
           	 	System.out.print("ERROR fetching content: " + e.getMessage());
            	
           	 	synchronized(UiApplication.getEventLock()){
    				UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
    					UiApplication.getUiApplication().pushScreen(
    							new LoginStatusScreen(e.getMessage()));
    		    }
           }
		}
	}
	
}
