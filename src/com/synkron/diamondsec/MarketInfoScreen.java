package com.synkron.diamondsec;

import net.rim.device.api.ui.Color;


public class MarketInfoScreen extends SubScreen {

	public MarketInfoScreen() {
		super();
		super._hManager.add(new CustomButtonField("Market Info", Color.DARKBLUE));
		super.setupStatusCommands();	
	}
}
