package com.synkron.diamondsec;

import java.util.Date;

import com.samples.toolkit.ui.component.PillButtonField;
import com.samples.toolkit.ui.container.NegativeMarginVerticalFieldManager;
import com.samples.toolkit.ui.container.PillButtonSet;
import com.samples.toolkit.ui.test.ForegroundManager;
import com.synkron.diamondsec.connectors.InfoWareConnector;
import com.synkron.diamondsec.connectors.OpenOrdersConnector;
import com.synkron.diamondsec.connectors.OrderHistoryConnector;
import com.synkron.diamondsec.utils.DataContext;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class OrderHistoryScreen extends SubScreen implements FieldChangeListener {
	public Manager _notSettled, _allHistory, _currentBody;
	
	PillButtonSet _pillButtonSet;
	PillButtonField _pillNotSettled;
	PillButtonField _pillAllHistory;
	Manager _bodyWrapper;
	
	public OrderHistoryScreen() {
		super();
		_hManager.add(new CustomButtonField("Order History", Color.DARKBLUE));
		setupStatusCommands();
		
		Manager foreground = new ForegroundManager();
		
		_pillButtonSet = new PillButtonSet();
		_pillNotSettled = new PillButtonField("Open Orders");
		_pillAllHistory = new PillButtonField("All History");
		
		_pillButtonSet.add(_pillNotSettled);
		_pillButtonSet.add(_pillAllHistory);
		_pillButtonSet.setMargin( 15, 15, 5, 15 );
		
		add(_pillButtonSet);
		
		_bodyWrapper = new NegativeMarginVerticalFieldManager( USE_ALL_WIDTH );
		
		_notSettled = new VerticalFieldManager();
		_notSettled.setMargin(20,0,0,0);
		
		_allHistory = new VerticalFieldManager();
		_allHistory.setMargin(20,0,0,0);
		
		_pillButtonSet.setSelectedField(_pillNotSettled);
		
		_currentBody = _notSettled;
		_bodyWrapper.add(_currentBody);
		
		_pillNotSettled.setChangeListener(new FieldChangeListener(){

			public void fieldChanged( Field field, int context ) {

				loadOpenOrders();
            	if( _currentBody != _notSettled ) {
	                _bodyWrapper.replace( _currentBody, _notSettled );
	                _currentBody = _notSettled;
            	}
            }
		});
		
		_pillAllHistory.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				Date date = new Date();
				DataContext dbContext = new DataContext();
				//Today
				String strEndDate =  dateFormat.format(date);
				String strFromDate = "01-JAN-1900";
				String strCustomerId = (String)dbContext.get("CustomerID");
				
				String _Url = InfoWareConnector.API_CUSTOMER_STATEMENT_URL;
				_Url = _Url+strCustomerId+"|"+strFromDate+"|"+strEndDate;
				OrderHistoryConnector _connector = new OrderHistoryConnector(_Url);
				_connector.start();
				
            	if( _currentBody != _allHistory ) {
	                _bodyWrapper.replace( _currentBody, _allHistory );
	                _currentBody = _allHistory;
            	}
            }
		});
		loadOpenOrders();
		foreground.add(_bodyWrapper);
		add(foreground);
	}
	
	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
	}
	
	public void loadOpenOrders(){
		DataContext dbContext = new DataContext();
		String strCustomerId = (String)dbContext.get("CustomerID");
		String _Url = InfoWareConnector.API_CUSTOMER_OPEN_ORDERS_URL;
		
		_Url = _Url+strCustomerId;
		OpenOrdersConnector _connector = new OpenOrdersConnector(_Url);
		_connector.start();
	}
}
