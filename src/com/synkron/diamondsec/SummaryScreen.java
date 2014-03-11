package com.synkron.diamondsec;

import com.samples.toolkit.ui.component.PillButtonField;
import com.samples.toolkit.ui.container.ListStyleButtonSet;
import com.samples.toolkit.ui.container.NegativeMarginVerticalFieldManager;
import com.samples.toolkit.ui.container.PillButtonSet;
import com.samples.toolkit.ui.test.ForegroundManager;
import com.synkron.diamondsec.connectors.MyStocksConnector;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

public class SummaryScreen extends SubScreen implements FieldChangeListener {
	Manager _summary;
	Manager _myStocks;
	Manager _bodyWrapper;
	Manager _currentBody;
	
	PillButtonSet _pillButtonSet;
	PillButtonField _pillSummary;
	PillButtonField _pillMyStocks;
	
	CustomButtonField btnLogOff, btnMyAccount;
	
	public SummaryScreen() {

		//super(MainScreen.VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super();
		
		_hManager.add(new CustomButtonField("My Account", Color.DARKBLUE));
		
		Manager foreground = new ForegroundManager();
		
		_pillButtonSet = new PillButtonSet();
		_pillSummary = new PillButtonField("Summary");
		_pillMyStocks = new PillButtonField("My Stocks");
		
		_pillButtonSet.add(_pillSummary);
		_pillButtonSet.add(_pillMyStocks);
		_pillButtonSet.setMargin( 15, 15, 5, 15 );
		
		add(_pillButtonSet);
		
		_bodyWrapper = new NegativeMarginVerticalFieldManager( USE_ALL_WIDTH );
		
		_myStocks = new ListStyleButtonSet();
		_myStocks.setChangeListener(new FieldChangeListener(){
			public void fieldChanged(Field field, int context) {
				String _Url = "";
				MyStocksConnector _connector = new MyStocksConnector(_Url);
				_connector.start();
			}
		});
		//_myStocks.add(new ListStyleButtonField("Ticker", 0));
		
		_summary = new ListStyleButtonSet();
		
		GridFieldManager _grdSummary = new GridFieldManager(5,2,0); 
		_grdSummary.add(new LabelField("Cash Balance"));
		_grdSummary.add(new LabelField("9,999,999.00"));
		_grdSummary.add(new LabelField("Equities"));
		_grdSummary.add(new LabelField("4,254,545.00"));
		_grdSummary.add(new LabelField("Fixed Income"));
		_grdSummary.add(new LabelField("2,343,543.00"));
		_grdSummary.add(new LabelField("Real Estate"));
		_grdSummary.add(new LabelField("20,324,665.00"));
		_grdSummary.add(new LabelField("Portfolio Value"));
		_grdSummary.add(new LabelField("5,545,566.21"));
		
		_grdSummary.setColumnPadding(20);
		_grdSummary.setRowPadding(20);
		_grdSummary.setMargin(0, 0, 20, 0);
		
		_summary.add(_grdSummary);
		
		GridFieldManager _grdSubSummary = new GridFieldManager(2,2,0); 
		_grdSubSummary.add(new LabelField("Funds Available for Trading"));
		_grdSubSummary.add(new LabelField("1,334,245.00"));
		_grdSubSummary.add(new LabelField("Margin Allowed"), Field.FIELD_LEFT);
		_grdSubSummary.add(new LabelField("4,254,545.00"));
		
		_grdSubSummary.setColumnPadding(20);
		_grdSubSummary.setRowPadding(20);
		_grdSubSummary.setMargin(0, 0, 20, 0);
		
		_summary.add(_grdSubSummary);
		
		//_summary.add(new ListStyleButtonField("Cash Balance",0));
		
		_pillButtonSet.setSelectedField(_pillSummary);
		
		_currentBody = _summary;
		_bodyWrapper.add(_currentBody);
		
		_pillSummary.setChangeListener(	new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {
            	if( _currentBody != _summary ) {
	                _bodyWrapper.replace( _currentBody, _summary );
	                _currentBody = _summary;
            	}
            }
		});
		
		_pillMyStocks.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {
            	if( _currentBody != _myStocks ) {
	                _bodyWrapper.replace( _currentBody, _myStocks );
	                _currentBody = _myStocks;
            	}
            }
		});
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

}
