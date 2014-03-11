package com.synkron.diamondsec;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ForgotPasswordScreen extends BaseScreen {

	public ForgotPasswordScreen() {
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		setTitle();
		setStatus();
		
		VerticalFieldManager _vManager = new VerticalFieldManager(USE_ALL_WIDTH | Manager.FIELD_HCENTER);
    	Bitmap header = Bitmap.getBitmapResource("forgotpass.png");
    	BitmapField _bmpField = new BitmapField(header, Field.FIELD_HCENTER);
    	
    	_vManager.add(_bmpField);
    	
    	add(_vManager);
	}
	
	public boolean onClose(){
        setDirty(false);
        return super.onClose();
    }
}
