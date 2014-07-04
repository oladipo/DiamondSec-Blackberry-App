package com.synkron.diamondsec;

import java.util.Date;

import com.samples.toolkit.ui.component.PillButtonField;
import com.samples.toolkit.ui.container.NegativeMarginVerticalFieldManager;
import com.samples.toolkit.ui.container.PillButtonSet;
import com.samples.toolkit.ui.test.ForegroundManager;
import com.synkron.diamondsec.connectors.InfoWareConnector;
import com.synkron.diamondsec.connectors.MyAccountConnector;
import com.synkron.diamondsec.connectors.MyStocksConnector;
import com.synkron.diamondsec.utils.DataContext;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;

public class SummaryScreen extends SubScreen implements FieldChangeListener {
	public Manager _summary;
	public Manager _myStocks;
	Manager _bodyWrapper;
	Manager _currentBody;
	
	PillButtonSet _pillButtonSet;
	PillButtonField _pillSummary;
	PillButtonField _pillMyStocks;
	
	CustomButtonField btnLogOff, btnMyAccount;
	DataContext _dbContext;
	String strCustomerId, strCscsNumber, strCustomerName;
	
	CustomLabelField lblCSCSNumber;
	
	public SummaryScreen() {

		//super(MainScreen.VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super();
		
		_hManager.add(new CustomButtonField("My Account", Color.DARKBLUE));
		
		Manager foreground = new ForegroundManager();
		
		_dbContext = new DataContext();
		strCustomerId = (String)_dbContext.get("CustomerID");
		
		_pillButtonSet = new PillButtonSet();
		_pillSummary = new PillButtonField("Summary");
		_pillMyStocks = new PillButtonField("My Stocks");

		_pillButtonSet.add(_pillSummary);
		_pillButtonSet.add(_pillMyStocks);
		_pillButtonSet.setMargin( 15, 15, 5, 15 );
		
		add(_pillButtonSet);
		
		_bodyWrapper = new NegativeMarginVerticalFieldManager( USE_ALL_WIDTH );
		
		//_myStocks = new ListStyleButtonSet();
		_myStocks = new VerticalFieldManager();
		_myStocks.setMargin(20,0,0,0);
		_summary = new VerticalFieldManager(USE_ALL_HEIGHT |USE_ALL_WIDTH);
		_summary.setMargin(0,0,0,0);
		
		_pillButtonSet.setSelectedField(_pillSummary);
		
		_currentBody = _summary;
		_bodyWrapper.add(_currentBody);
		
		_pillSummary.setChangeListener(	new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {

				loadAccountSummary();
				
				if( _currentBody != _summary ) {
	                _bodyWrapper.replace( _currentBody, _summary );
	                _currentBody = _summary;
            	}
            }
		});
		
		_pillMyStocks.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				Date date = new Date();
				String strValueDate =  dateFormat.format(date);
				
				String _Url = InfoWareConnector.API_CUSTOMER_PORTFOLIO_HOLDINGS_URL;
				_Url = _Url+strCustomerId+"|"+strValueDate;
				MyStocksConnector _connector = new MyStocksConnector(_Url);
				_connector.start();
				//UiApplication.getUiApplication().pushScreen(new LoginStatusScreen());
				if( _currentBody != _myStocks ) {
	                _bodyWrapper.replace( _currentBody, _myStocks );
	                _currentBody = _myStocks;
            	}
            	
            }
		});
		loadAccountSummary();
        foreground.add( _bodyWrapper );
        add( foreground );
        
        super.setupStatusCommands();
	}


	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
		if(field == btnLogOff){
			//Dialog.alert("Login Failed");
			UiApplication diamondSec = UiApplication.getUiApplication();
			diamondSec.pushScreen(new LoginScreen());
		}
	}

	public void loadAccountSummary(){
		String _Url = InfoWareConnector.API_CUSTOMER_ACCOUNT_SUMMARY_URL;
		_Url = _Url+strCustomerId;
		MyAccountConnector _connector = new MyAccountConnector(_Url);
		_connector.start();
	}
}
