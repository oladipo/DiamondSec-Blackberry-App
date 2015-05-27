package com.synkron.diamondsec.connectors;

import javax.microedition.io.*;
import net.rim.device.api.io.transport.*;
import net.rim.device.api.io.transport.options.BisBOptions;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.util.Arrays;

public class InfoWareConnector extends Thread implements net.rim.device.api.system.CoverageStatusListener{
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
	public static final String API_CUSTOMER_STATEMENT_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000001/";
	public static final String API_CUSTOMER_OPEN_ORDERS_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000014/";
	public static final String API_CUSTOMER_TRANSACTION_HISTORY_URL = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000015/";
	public static final String API_ALL_TOP_GAINERS_LAST_TRADING_DAY = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000016";
	public static final String API_ALL_TOP_LOOSERS_LAST_TRADING_DAY = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000017";
	public static final String API_ASI_MARKET_INFORMATION = "http://www.infowarelimited.com/svcs/svcs01/IWDataSvc.svc/json/1101/1325/1000018";
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
	
	public boolean _mdsSupport = false;
	public boolean _bisSupport = false;
	
	public InfoWareConnector(String Url){
		
		 CoverageInfo.addListener(this);
		 
		if(_bisSupport){
			this._Url = Url + ";deviceside=false;ConnectionType=mds-public";
		}else if(_mdsSupport){
			this._Url = Url + ";deviceside=false";
		}else{
			this._Url = Url + ";deviceside=true";
		}
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
		_factory.setTransportTypeOptions(TransportInfo.TRANSPORT_BIS_B, new BisBOptions("MDS_PUBLIC"));
		//_factory.setTransportTypeOptions(TransportInfo.TRANSPORT_BIS_B, new BisBOptions("FQ2y34b6")); 
		// Set ConnectionFactory options.
        if(_transports.length > 0)
        {
            _factory.setPreferredTransportTypes(_transports);
        }
	}
	
    private void setCoverage() {
    	if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS)) {
       		_mdsSupport = true;
    	}
    	if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B)) {
    		_bisSupport = true;
    	}
    }
    
    /**
     * This method handles changes in Coverage through the CoverageStatusListener interface.
     * CoverageStatusListener works with CoverageInfo and is available with 4.2.0 
     */
    public void coverageStatusChanged(int newCoverage) {
    	if ((newCoverage & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS) {
        	_mdsSupport = true;
    	}
    	if ((newCoverage & CoverageInfo.COVERAGE_BIS_B) == CoverageInfo.COVERAGE_BIS_B) {
    		_bisSupport = true;
    	}
    }
    

    /**
     * This method provides the functionality of actually parsing 
     * through the service books on the handheld and determining
     * which traffic routes are available based on that information.
     * Before 4.2.0, this method is necessary to determine coverage.
     */
    private void parseServiceBooks()
    {
        // Add in our new items by scrolling through the ServiceBook API.
        ServiceBook sb = ServiceBook.getSB();
        ServiceRecord[] records = sb.findRecordsByCid("IPPP");      // The IPPP service represents the data channel for MDS and BIS-B
        if( records == null ) {
            return;
        }
        
        int numRecords = records.length;
        for( int i = 0; i < numRecords; i++ ) {
            ServiceRecord myRecord = records[i];
            String name = myRecord.getName();       // Technically, not needed but nice for debugging.
            String uid = myRecord.getUid();         // Technically, not needed but nice for debugging.

            // First of all, the CID itself should be equal to IPPP if this is going to be an IPPP service book.
            if( myRecord.isValid() && !myRecord.isDisabled() ) {
                // Now we need to determine if the service book is Desktop or BIS.  One could check against the
                // name but that is unreliable.  The best mechanism is to leverage the security of the service
                // book to determine the security of the channel.
                int encryptionMode = myRecord.getEncryptionMode();
                if( encryptionMode == ServiceRecord.ENCRYPT_RIM ) {
                    _mdsSupport = true;
                } else {
                    _bisSupport = true;
                }
            }
        }
    }
	//Sub Classes will override run
	public void run(){
		
	}

	public void coverageStatusChanged(int[] arg0) {
		// TODO Auto-generated method stub
		
	}

}
