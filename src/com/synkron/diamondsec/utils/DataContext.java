package com.synkron.diamondsec.utils;

import java.util.Hashtable;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class DataContext {
	
	PersistentObject _persistentObject;
	Hashtable _customerTable;
	
	public DataContext(){
		_persistentObject = PersistentStore.getPersistentObject(0x9efd771bdee6d5adL);
		
		synchronized(_persistentObject){
			_customerTable = (Hashtable)_persistentObject.getContents();
			if(null == _customerTable){
				_customerTable = new Hashtable();
				_persistentObject.setContents(_customerTable);
				_persistentObject.commit();
			}
		}
	}
	
	public Object get(String key){
		return _customerTable.get(key);
	}
	
	public void set(String key, Object value){
		_customerTable.put(key, value);
	}
	
	public void commit(){
		_persistentObject.commit();
	}
}
