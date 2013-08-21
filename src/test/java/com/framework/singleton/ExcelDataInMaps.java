/**
 * This singleton Class would register the sheets and retrieve the data
 */
package com.framework.singleton;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author surendrane
 *
 */
public class ExcelDataInMaps {
	
	private static ExcelDataInMaps instance;
	HashMap<String,Map<Object,Object>> excelData = new HashMap<String,Map<Object,Object>>();
	HashMap<String,String[][]> dataProviderData = new HashMap<String, String[][]>();
	
	ReadExcelSheet readSheet = new ReadExcelSheet();
	LinkedHashMap<String,String> registeredSheets = null;
	Map<String,String[]> recessMap = null;
	RegisterSheetsInExcel register = RegisterSheetsInExcel.getInstance();
	
	public static synchronized ExcelDataInMaps getInstance()
	{
		if (instance == null)
		  {
			instance = new ExcelDataInMaps();
		  }
		  return instance;
	}

	/**
	 * Constructor -- to register sheets and extract data from them
	 */
	public ExcelDataInMaps() {
		// TODO Auto-generated constructor stub
		
		//Register the provided sheets
		register.registerSheets();
				
		registeredSheets = (LinkedHashMap<String, String>) register.getRegisteredSheets();
		
		//Retrieves data from the registered sheets
		retrieveDataFromRegisteredSheets();
	}

	/**
	 * @return the excelData
	 */
	public HashMap<String, Map<Object, Object>> getExcelData() {
		return excelData;
	}
	
	public HashMap<String,String[][]> getDataProviderData()
	{
		return dataProviderData;
	}

	/**
	 * Retrieves data from registered sheets
	 */
	public void retrieveDataFromRegisteredSheets()
	{
		for (Entry<String, String> entry : registeredSheets.entrySet())
		{
			if(register.getDataProviderSheets().containsKey(entry.getKey()))
				dataProviderData.put(entry.getKey(), readSheet.getDataForDataProvider(entry.getKey()));
			else
				excelData.put(entry.getKey(), readSheet.getInfoFromExcelSheet(entry.getKey()));
		}
	}

}
