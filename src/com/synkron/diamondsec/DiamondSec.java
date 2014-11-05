package com.synkron.diamondsec;

import com.samples.toolkit.ui.test.UIExampleIndexScreen;
import com.synkron.diamondsec.connectors.InfoWareConnector;
import com.synkron.diamondsec.services.TickerUpdateService;

import net.rim.device.api.system.Alert;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class DiamondSec extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
    	if(args != null && args.length > 0 && "ticker".equals(args[0])){
    		//scheduleUpdate();
    		
    	}else{
	        DiamondSec theApp = new DiamondSec();       
	        theApp.enterEventDispatcher();
    	}
    }
    
    private static void scheduleUpdate() {
		// TODO Auto-generated method stub
    	Alert.startVibrate(2550);
        
    	ApplicationDescriptor current = ApplicationDescriptor.currentApplicationDescriptor();
    	
        current.setPowerOnBehavior(ApplicationDescriptor.DO_NOT_POWER_ON);
        
        ApplicationManager manager = ApplicationManager.getApplicationManager();
        
        //check if device has booted and is ready..
        if(!manager.inStartup()){
        	try{
        		TickerUpdateService tickerUpdater = new TickerUpdateService(InfoWareConnector.API_FULL_PRICE_LIST_URL);
        		tickerUpdater.start();
        	}catch(Exception Ex){
        		System.out.println(Ex.getMessage());
        	}
        }
        
        manager.scheduleApplication(current, System.currentTimeMillis() + 60000, true);
	}

	/**
     * Creates a new DiamondSec object
     */
    public DiamondSec()
    {        
        // Push the splash screen onto the UI stack for rendering.
    	//pushScreen(new SplashScreen(UiApplication.getUiApplication(), new LoginScreen()));
    	pushScreen(new LoginScreen());
    	//pushScreen(new UIExampleIndexScreen());
    }    
}