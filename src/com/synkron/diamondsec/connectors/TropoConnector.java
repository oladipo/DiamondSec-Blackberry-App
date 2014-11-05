package com.synkron.diamondsec.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connection;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

import com.synkron.diamondsec.LoginStatusScreen;

import net.rim.device.api.io.transport.ConnectionDescriptor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.io.transport.options.BisBOptions;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.Arrays;

public class TropoConnector extends Thread{
	public String _Url, _Result;
	public int[] _transports;
	protected ConnectionFactory _factory;
	protected ConnectionDescriptor _descriptor;
	protected Connection _connection;
	
	//BEGIN API ENDPOINTS
	public static final String API_CALL_ENDPOINT = "https://api.tropo.com/1.0/sessions?action=create&token=59280ccb7cbdff4a8984d706a82057de41aea08d48c3f414fbefa12af51bbffcc7ca5d6b6806fa82b95cb9ed";
	
	//END API ENDPOINTS
	
	
	public TropoConnector(String Url){
		this._Url = Url;	
		_transports = new int[]{
				TransportInfo.TRANSPORT_TCP_WIFI,
				TransportInfo.TRANSPORT_WAP2,
				TransportInfo.TRANSPORT_TCP_CELLULAR,
				TransportInfo.TRANSPORT_BIS_B,
				TransportInfo.TRANSPORT_MDS,
				TransportInfo.TRANSPORT_WAP
			};
		
        // Remove any transports that are not currently available.
        for(int i = 0; i < _transports.length ; i++)
        {
            int transport = _transports[i];
            if(!TransportInfo.isTransportTypeAvailable(transport)
                  || !TransportInfo.hasSufficientCoverage(transport))
            {
                Arrays.removeAt(_transports, i);
            }
        }
        String _available = "Available Connections : ";
        for(int i=0; i < _transports.length; i++){
        	//_available.concat(" \n"+Integer.toString(_transports[i]));
        	_available = _available + "\n "+_transports[i];
        }
        
		_factory = new ConnectionFactory();
		_factory.setConnectionTimeout(10000);
		_factory.setAttemptsLimit(5);
		_factory.setTimeLimit(10000);
		_factory.setTransportTypeOptions(TransportInfo.TRANSPORT_BIS_B, new BisBOptions("mds-public"));
		//_factory.setTransportTypeOptions(TransportInfo.TRANSPORT_BIS_B, new BisBOptions("FQ2y34b6")); 
		// Set ConnectionFactory options.
        if(_transports.length > 0)
        {
            _factory.setPreferredTransportTypes(_transports);
        }
	}
	
	//Sub Classes will override run
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
			}catch(IOException e){
				
				e.printStackTrace();
				
				synchronized(UiApplication.getEventLock()){
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
	        				
	        					UiApplication.getUiApplication().pushScreen(
	        							new LoginStatusScreen(e.getMessage()));
	        		    }
	                }
	                catch(Exception e){
		               	 	System.out.print("ERROR fetching content: " + e.getMessage());
		        			
		               	 	synchronized(UiApplication.getEventLock()){
		        				
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
	    				
	    					UiApplication.getUiApplication().pushScreen(
	    							new LoginStatusScreen(e.getMessage()));
	    		    }
	           }
			}

	}
}
