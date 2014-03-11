package com.synkron.diamondsec;

import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class LoginStatusScreen extends PopupScreen {

	public LoginStatusScreen() {
		super(new VerticalFieldManager());
		//LabelField labelField = new LabelField("Popup Screen",
		//		Field.FIELD_HCENTER);
		//add(labelField);

		GIFEncodedImage status = (GIFEncodedImage)GIFEncodedImage.getEncodedImageResource("loading154.gif");
		AnimatedGIFField _theAnimation = new AnimatedGIFField(status);
    	
		add(_theAnimation);
		// TODO Auto-generated constructor stub
	}

	public LoginStatusScreen(String Message) {
		super(new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL | VERTICAL_SCROLLBAR | USE_ALL_HEIGHT | USE_ALL_WIDTH));
	
		LabelField labelField = new LabelField(Message,
				Field.FIELD_HCENTER);
		labelField.setMargin(20,20,20,20);
		add(labelField);
		

		CustomButtonField _btnClose = new CustomButtonField("Ok", Color.DARKBLUE);
		_btnClose.setChangeListener(new FieldChangeListener(){

			public void fieldChanged(Field field, int context) {
				UiApplication diamondSec = UiApplication.getUiApplication();
					diamondSec.popScreen(diamondSec.getActiveScreen());
			}
			
		});
		
		add(_btnClose);
		// TODO Auto-generated constructor stub
	}
}
