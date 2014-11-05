package com.synkron.diamondsec.utils;

import net.rim.device.api.database.Cursor;
import net.rim.device.api.database.Database;
import net.rim.device.api.database.DatabaseException;
import net.rim.device.api.database.DatabaseFactory;
import net.rim.device.api.database.DatabaseIOException;
import net.rim.device.api.database.DatabasePathException;
import net.rim.device.api.database.Statement;
import net.rim.device.api.io.MalformedURIException;
import net.rim.device.api.io.URI;

public class MarketDatabase {
	Database _myDb;
	public Statement _createStatement, _insertStatement, _selectStatement, _deleteStatement;
	
	public MarketDatabase(){
		_myDb = OpenOrCreate("StocksDB");
		//InitializeQuery();
		CreateTable("Stocks");
	}
	
	public Database OpenOrCreate(String strDataBaseName){
		try{
			URI dbPath = URI.create("file:///SDCard/databases/Diamond_Securities/"+strDataBaseName);
			_myDb = DatabaseFactory.openOrCreate(dbPath);
			
		}catch(DatabaseIOException Ex){
			
			System.out.print(Ex.getMessage());

		}
		catch(DatabasePathException Ex){
			System.out.print(Ex.getMessage());
		}
		catch(MalformedURIException Ex){
			System.out.print(Ex.getMessage());
		}
		finally{
			try {
				_myDb.close();
			} catch (DatabaseIOException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
		}
		return _myDb;
	}
	public Cursor SelectRecords(){
		Cursor theCursor = null;
		try {
			_selectStatement = _myDb.createStatement("SELECT * FROM Stocks");
			_selectStatement.prepare();
			
			theCursor = _selectStatement.getCursor();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				_myDb.close();
			} catch (DatabaseIOException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
		}
		return theCursor;
	}
	
	public Cursor Find(String strParam){
		Cursor theCursor = null;
		try {
			_selectStatement = _myDb.createStatement("SELECT * FROM Stocks where Name like '%"+strParam+"%'");
			_selectStatement.prepare();
			
			theCursor = _selectStatement.getCursor();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				_myDb.close();
			} catch (DatabaseIOException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
		}
		return theCursor;
	}
	public void CreateTable(String strTableName){
		try{
			_createStatement = _myDb.createStatement("CREATE TABLE IF NOT EXISTS Stocks ('Ticker' TEXT, " +
			        "'Name' TEXT, " + 
			        "'Price' TEXT," +
			        "'Change' TEXT)");  
			
			_createStatement.prepare();
			_createStatement.execute();
			_createStatement.close();
			
		}catch(DatabaseException Ex){
			System.out.print(Ex.getMessage());
		}finally{
			try {
				_myDb.close();
			} catch (DatabaseIOException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
		}
	}
	public void InsertStock(String strTicker, String strName, String strPrice, String strChange){
		try {
			_insertStatement = _myDb.createStatement("INSERT INTO Stocks(Ticker," +
			"Name,Price,Change)"+ 
			"VALUES('"+strTicker+"','"+strName+"','"+strPrice.replace('\n', ' ')+"','"+strChange+"')");
			
			_insertStatement.prepare();
			_insertStatement.execute();
			
			_insertStatement.close();
			
		} catch (DatabaseException e) {

			e.printStackTrace();
		}finally{
			try {
				_myDb.close();
			} catch (DatabaseIOException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
		}
	}
	public void InitializeQuery(){
	}

	public void Close() {
		// TODO Auto-generated method stub
		try {
			_myDb.close();
		} catch (DatabaseIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				_myDb.close();
			} catch (DatabaseIOException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
		}
	}
	
	public void DeleteStocks(){
		try {
			_deleteStatement = _myDb.createStatement("DELETE from Stocks");
			
			_deleteStatement.prepare();
			_deleteStatement.execute();
			
			_deleteStatement.close();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				_myDb.close();
			} catch (DatabaseIOException e) {
				// TODO Auto-generated catch block
				System.out.print(e.getMessage());
			}
		}
	}
}
