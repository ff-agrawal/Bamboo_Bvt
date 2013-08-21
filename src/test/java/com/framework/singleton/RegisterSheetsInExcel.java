/**
 * Registering Excel sheets in the workbook
 */
package com.framework.singleton;

import java.util.LinkedHashMap;
import java.util.Map;

import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class RegisterSheetsInExcel {
	
	private static RegisterSheetsInExcel instance;
	
	private Map<String, String> registerSheet = new LinkedHashMap<String, String>();
	private Map<String, String> sheetData = new LinkedHashMap<String, String>(); 
	private Map<String,String> dataProviderSheets = new LinkedHashMap<String,String>();
	
	ConfigUtil configUtil = ConfigUtil.getConfigUtil();
	
	public static synchronized RegisterSheetsInExcel getInstance()
	{
		if (instance == null)
		  {
			instance = new RegisterSheetsInExcel();
		  }
		  return instance;
	}
	
	public void registerSheets()
	{
		registerSheet.put(configUtil.getProperty(Constants.SHEET_NAME), configUtil.getProperty(Constants.BUILDING_INFO_FILE));
		registerSheet.put(configUtil.getProperty(Constants.HOLIDAY_SHEET), configUtil.getProperty(Constants.BUILDING_INFO_FILE));
		registerSheet.put(configUtil.getProperty(Constants.RECOMMENDATION_SHEET), configUtil.getProperty(Constants.BUILDING_INFO_FILE));
		registerSheet.put(configUtil.getProperty(Constants.THERMAL_SHEET), configUtil.getProperty(Constants.BUILDING_INFO_FILE));
		registerSheet.put(configUtil.getProperty(Constants.Disaggregation_Sheet), configUtil.getProperty(Constants.BUILDING_INFO_FILE));
				
		//Set Data types
		setSheetsDataType();
		
		//Set Data provider Sheets
		setdataProviderSheets();
	}
	
	public void setSheetsDataType()
	{
		sheetData.put(configUtil.getProperty(Constants.SHEET_NAME), "single");
		sheetData.put(configUtil.getProperty(Constants.HOLIDAY_SHEET), "multiple");
		sheetData.put(configUtil.getProperty(Constants.THERMAL_SHEET),"single");
		sheetData.put(configUtil.getProperty(Constants.Disaggregation_Sheet),"single");
	}
	
	public void setdataProviderSheets()
	{
		dataProviderSheets.put(configUtil.getProperty(Constants.RECOMMENDATION_SHEET), configUtil.getProperty(Constants.BUILDING_INFO_FILE));
	}
	
	public Map<String,String> getRegisteredSheets()
	{
		return registerSheet;
	}
	
	public Map<String,String> getDataTypeOfSheet()
	{
		return sheetData;
	}
	
	public Map<String,String> getDataProviderSheets()
	{
		return dataProviderSheets;
	}

}
