package com.synkron.diamondsec;

import com.samples.toolkit.ui.component.*;
import com.samples.toolkit.ui.container.*;
import com.samples.toolkit.ui.test.ForegroundManager;
import com.synkron.diamondsec.connectors.*;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class MarketInfoScreen extends SubScreen {
	public Manager _allShareIndex, _topGainers, _topLoosers;
	Manager _bodyWrapper,_currentBody;
	
	PillButtonSet _pillButtonSet;
	PillButtonField _pillAllShareIndex, _pillTopGainers, _pillTopLoosers;
	
	public MarketInfoScreen() {
		super();
		super._hManager.add(new CustomButtonField("Market Info", Color.DARKBLUE));
		Manager foreground = new ForegroundManager();
		
		_pillButtonSet = new PillButtonSet();
		_pillButtonSet.setMargin( 15, 15, 5, 15 );
		
		_pillAllShareIndex = new PillButtonField("Index");
		_pillTopGainers = new PillButtonField("Top Gainers");
		_pillTopLoosers = new PillButtonField("Top Losers");
		
		_pillButtonSet.add(_pillAllShareIndex);
		_pillButtonSet.add(_pillTopGainers);
		_pillButtonSet.add(_pillTopLoosers);
		
		add(_pillButtonSet);
		
		_bodyWrapper = new NegativeMarginVerticalFieldManager( USE_ALL_WIDTH );
		
		_allShareIndex = new VerticalFieldManager(USE_ALL_HEIGHT |USE_ALL_WIDTH);
		_allShareIndex.setMargin(20,0,0,0);
		
		_topGainers = new VerticalFieldManager(USE_ALL_HEIGHT |USE_ALL_WIDTH);
		_topGainers.setMargin(20,0,0,0);
		
		_topLoosers = new VerticalFieldManager(USE_ALL_HEIGHT |USE_ALL_WIDTH);
		_topLoosers.setMargin(20,0,0,0);

		_pillButtonSet.setSelectedField(_pillAllShareIndex);
		
		_currentBody = _allShareIndex;
		_bodyWrapper.add(_currentBody);
		
		_pillAllShareIndex.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {

				loadASIMarketInformation();
				
				if( _currentBody != _allShareIndex ) {
	                _bodyWrapper.replace( _currentBody, _allShareIndex );
	                _currentBody = _allShareIndex;
            	}
            }
		});
		
		_pillTopGainers.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {

				loadTopGainers();
				
				if( _currentBody != _topGainers ) {
	                _bodyWrapper.replace( _currentBody, _topGainers );
	                _currentBody = _topGainers;
            	}
            }
		});
		
		_pillTopLoosers.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {

				loadTopLosers();
				
				if( _currentBody != _topLoosers ) {
	                _bodyWrapper.replace( _currentBody, _topLoosers );
	                _currentBody = _topLoosers;
            	}
            }
		});
		super.setupStatusCommands();
		
		loadASIMarketInformation();
		
		foreground.add(_bodyWrapper);
		add(foreground);
		
	}

	protected void loadASIMarketInformation() {
		String _Url = InfoWareConnector.API_ASI_MARKET_INFORMATION;
		
		ASIMarketInfoConnector _connector = new ASIMarketInfoConnector(_Url);
		_connector.start();
	}
	
	protected void loadTopGainers() {
		String _Url = InfoWareConnector.API_ALL_TOP_GAINERS_LAST_TRADING_DAY;
		
		TopGainersConnector _connector = new TopGainersConnector(_Url);
		_connector.start();
	}
	
	protected void loadTopLosers(){
		String _Url = InfoWareConnector.API_ALL_TOP_LOOSERS_LAST_TRADING_DAY;
		
		TopLosersConnector _connector = new TopLosersConnector(_Url);
		_connector.start();	
	}
}
