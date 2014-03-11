package com.synkron.diamondsec.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class TradingBuyConnector extends InfoWareConnector{

	public TradingBuyConnector(String Url) {
		super(Url);
		// TODO Auto-generated constructor stub
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
	            
	            try {
					JSONObject obj = new JSONObject(_Result);
					JSONArray jsonArray = obj.getJSONArray("Rows");
					
					for(int i = 0; i < jsonArray.length(); i++){
						JSONArray inner  = jsonArray.getJSONArray(i);
						for(int j = 0; j < inner.length() ; j++){
							
						}

					}
	            }catch(JSONException e){
	            	e.printStackTrace();
	            }
			}
			catch(Exception e){
				
			}
			finally{
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
	                }
	                catch(Exception e){
	                	 System.out.print("ERROR fetching content: " + e.getMessage());
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
	                }
	                catch(Exception e){
		               	 	System.out.print("ERROR fetching content: " + e.getMessage());
		     		
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

	            }
	            catch(Exception e){
	           	 	System.out.print("ERROR fetching content: " + e.getMessage());

	           }
			
			}
	
	}
}
