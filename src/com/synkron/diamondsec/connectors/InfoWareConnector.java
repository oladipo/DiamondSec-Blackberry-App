package com.synkron.diamondsec.connectors;

import javax.microedition.io.*;
import net.rim.device.api.io.transport.*;
import net.rim.device.api.io.transport.options.BisBOptions;
import net.rim.device.api.util.Arrays;

public class InfoWareConnector extends Thread{
	public String _Url, _Result;
	public int[] _transports;
	protected ConnectionFactory _factory;
	protected ConnectionDescriptor _descriptor;
	protected Connection _connection;
	
	//BEGIN API ENDPOINTS
	public static final String API_LOGIN_URL = "http://www.infowarelimited.com/svcs/svcs01/IWdataSvc.svc/json/1101/1325/1000010/";
	public static final String API_TRADEABLE_STOCKS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000011";
	public static final String API_CUSTOMER_ACCOUNT_SUMMARY_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/4/1101/1325/1000007/";
	public static final String API_CUSTOMER_PORTFOLIO_HOLDINGS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000002/";
	public static final String API_FULL_PRICE_LIST_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000005";
	public static final String API_MARKET_DATA_FOR_STOCK_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000013/";
	public static final String API_GET_CUSTOMER_CSCS_NUMBERS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000012/";
	public static final String API_TRADE_REQUEST_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/4/1101/1325/1000008/";
	public static final String API_PLACE_TRADE_ORDER_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/4/1101/1325/1000009/";
	//END API ENDPOINTS
	
	//BEGIN TRADE ACTION DEFINITION
	public static final int API_TRADE_ACTION_BUY = 0;
	public static final int API_TRADE_ACTION_SELL = 1;
	//END TRADE ACTION DEFINITION
	
	//BEGIN ORDER TYPES
	public static final int API_ORDER_TYPE_MARKET = 49;
	public static final int API_ORDER_TYPE_LIMIT = 50;
	public static final int API_ORDER_TYPE_STOP = 51;
	public static final int API_ORDER_TYPE_STOP_LIMIT = 52;
	//END ORDER TYPES
	
	//BEGIN TIME IN FORCE
	public static final int API_TIME_IN_FORCE_DAY = 0;
	public static final int API_TIME_IN_FORCE_GOODTILLCANCELLED = 1;
	public static final int API_TIME_IN_FORCE_IMMEDIATEORCANCEL = 3;
	public static final int API_TIME_IN_FORCE_FILLORKILL = 4;
	public static final int API_TIME_IN_FORCE_GOODTILLDATE = 6;
	//END TIME IN FORCE
	
	
	public InfoWareConnector(String Url){
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
		
	}

}
