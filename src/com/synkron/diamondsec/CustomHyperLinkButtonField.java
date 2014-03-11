package com.synkron.diamondsec;

import com.samples.toolkit.ui.component.HyperlinkButtonField;

public class CustomHyperLinkButtonField extends HyperlinkButtonField{

	public CustomHyperLinkButtonField( String label ) {
        super( label, 0x0000FF, 0xFFFFFF, 0x0000FF, 0, 0 );
        setPadding( 8, 5, 8, 5 );
    }

}
