package com.synkron.diamondsec;

import com.samples.toolkit.ui.test.UIExampleIndexScreen;

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
        DiamondSec theApp = new DiamondSec();       
        theApp.enterEventDispatcher();
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
