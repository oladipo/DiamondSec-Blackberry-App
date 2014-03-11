package com.synkron.diamondsec;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class CustomLabelField extends LabelField{
	 public CustomLabelField(Object text, long style) {
         super(text, style);
     }

	 public CustomLabelField() {
		 super();
     }
	 
     private int mFontColor = -1;

     public void setFontColor(int fontColor) {
         mFontColor = fontColor;
     }

     protected void paint(Graphics graphics) {
         if (-1 != mFontColor)
             graphics.setColor(mFontColor);
         super.paint(graphics);
     }
}
