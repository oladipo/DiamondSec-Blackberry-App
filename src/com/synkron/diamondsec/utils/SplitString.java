package com.synkron.diamondsec.utils;

import java.util.Vector;

public class SplitString {
	
	public static String[] split(String original, String separator) {

	    Vector nodes = new Vector();               
	    int index = original.indexOf(separator);      
	    while (index >= 0) {                   
	        nodes.addElement(original.substring(0, index));           
	        original = original.substring(index + separator.length());          
	        index = original.indexOf(separator);       
	    }       
	    nodes.addElement(original);              
	    String[] result = new String[nodes.size()];       
	    if (nodes.size() > 0) {           
	        for (int loop = 0; loop < nodes.size(); loop++) {               
	            result[loop] = (String) nodes.elementAt(loop);               
	            System.out.println("Value inside result is ........ "+ result[loop]);           
	        }       
	    }      
	    return result;   
	}
}
