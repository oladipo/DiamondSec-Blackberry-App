package com.synkron.diamondsec;

import com.samples.toolkit.ui.component.PillButtonField;
import com.samples.toolkit.ui.container.ListStyleButtonSet;
import com.samples.toolkit.ui.container.NegativeMarginVerticalFieldManager;
import com.samples.toolkit.ui.container.PillButtonSet;
import com.samples.toolkit.ui.test.ForegroundManager;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;

public class OrderHistoryScreen extends SubScreen implements FieldChangeListener {
	Manager _notSettled, _allHistory, _currentBody;
	
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
		_pillNotSettled = new PillButtonField("Not Settled");
		_pillAllHistory = new PillButtonField("All History");
		
		_pillButtonSet.add(_pillNotSettled);
		_pillButtonSet.add(_pillAllHistory);
		_pillButtonSet.setMargin( 15, 15, 5, 15 );
		
		add(_pillButtonSet);
		
		_bodyWrapper = new NegativeMarginVerticalFieldManager( USE_ALL_WIDTH );
		
		_notSettled = new ListStyleButtonSet();
		_allHistory = new ListStyleButtonSet();
		
		_pillButtonSet.setSelectedField(_pillNotSettled);
		
		_currentBody = _notSettled;
		_bodyWrapper.add(_currentBody);
		
		_pillNotSettled.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {
            	if( _currentBody != _notSettled ) {
	                _bodyWrapper.replace( _currentBody, _notSettled );
	                _currentBody = _notSettled;
            	}
            }
		});
		
		_pillAllHistory.setChangeListener(new FieldChangeListener(){
			
			public void fieldChanged( Field field, int context ) {
            	if( _currentBody != _allHistory ) {
	                _bodyWrapper.replace( _currentBody, _allHistory );
	                _currentBody = _allHistory;
            	}
            }
		});
		
		foreground.add(_bodyWrapper);
		add(foreground);
	}
	
	public void fieldChanged(Field field, int context) {
		// TODO Auto-generated method stub
	}
}
