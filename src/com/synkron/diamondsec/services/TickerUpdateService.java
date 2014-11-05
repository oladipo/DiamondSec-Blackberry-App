package com.synkron.diamondsec.services;

import java.io.*;

import javax.microedition.io.*;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.synkron.diamondsec.connectors.InfoWareConnector;
import com.synkron.diamondsec.utils.MarketDatabase;
import com.synkron.diamondsec.utils.SplitString;

public class TickerUpdateService extends InfoWareConnector{
	
	public TickerUpdateService(String Url) {
		super(Url);
	}

	public void run(){
			System.out.println("ticker update started");
			
			OutputStream oStream = null;
			InputStream iStream = null;
			
			try{			
				//retrieve stock update from infoware service
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
				
				//clear db tables.
				
				MarketDatabase _myDb = new MarketDatabase();
				_myDb.DeleteStocks();
				
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

						String _txtStockCode[] = SplitString.split(sb,"|");
						
						//write to database
						_myDb.InsertStock(_txtStockCode[0], _txtStockCode[1], _txtStockCode[2].trim(), _txtStockCode[3]);
						
						sb = "";
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}
				_myDb.Close();				

		}catch(Exception Ex){
			
			System.out.print(Ex.getMessage());
			
		}finally{
			
		}
					
		System.out.println("ticker update complete");
	}
}
