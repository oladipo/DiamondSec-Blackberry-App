package com.synkron.diamondsec;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public class BaseScreen extends MainScreen
{
    /**
     * Creates a new MainScreen object
     */
	VerticalFieldManager _manager;
    BaseScreen()
    {        
    	this(0);
    }
    
    BaseScreen(long style){
    	super(style);
    	
    	Background bg = BackgroundFactory.createSolidBackground(Color.DARKGRAY);
    	_manager = (VerticalFieldManager)getMainManager();
    	_manager.setBackground(bg);
    }
    
    public void setTitle(){
    	
    	//LabelField titleLabel = new LabelField(title);
    	//titleLabel.setPadding(4, 0, 3, 4);
    	//titleLabel.setFont(titleLabel.getFont().derive(Font.PLAIN, titleLabel.getFont().getHeight()+ 2));
    	
    	VerticalFieldManager _vManager = new VerticalFieldManager(USE_ALL_WIDTH | Manager.FIELD_HCENTER);
    	Background bg = BackgroundFactory.createSolidBackground(Color.WHITE);
    	_vManager.setBackground(bg);
    	
    	Bitmap header = Bitmap.getBitmapResource("header.png");
    	BitmapField _bmpField = new BitmapField(header, Field.FIELD_HCENTER);
    	
    	_vManager.add(_bmpField);
    	
    	super.setTitle(_vManager);
    }   

    public void setTitle(Field title){
    	super.setTitle(title);
    }
    
    public void setStatus(){
    	Background bg = BackgroundFactory.createSolidBackground(Color.DARKGRAY);
    	
    	VerticalFieldManager _vStatusManager = new VerticalFieldManager(USE_ALL_WIDTH | Manager.FIELD_HCENTER);
    	_vStatusManager.setBackground(bg);
    	
		CustomLabelField lblCopyright = new CustomLabelField("Copyright (c) Diamond Securities Limited",USE_ALL_WIDTH | FIELD_HCENTER);
		lblCopyright.setFontColor(Color.WHITE);
		lblCopyright.setMargin(0,0,20,0);
		_vStatusManager.add(lblCopyright);
		
		setStatus(_vStatusManager);
    }
    public void close(){
    	System.exit(0);
    }
}
