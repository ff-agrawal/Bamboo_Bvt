/**
 * This class would be the super class for all Tests
 */
package com.framework.library;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;

import com.framework.dataobjects.TestListener;
import com.framework.driverfactory.BrowserDriver;
import com.framework.driverfactory.WebDriverFactory;
import com.framework.singleton.ExcelDataInMaps;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class BaseClass extends LibraryClass{
	
	ExcelDataInMaps excel = ExcelDataInMaps.getInstance();
	TestListener listener = TestListener.getInstance();
	
	/**
	 * 
	 * @return WebDriver with respective to the Specified browser
	 * Default: IExplorer
	 */
	public WebDriver getWebDriver()
	{
		WebDriverFactory driverFactory = new WebDriverFactory();
		BrowserDriver browser = driverFactory.getBrowser();
		return browser.createDriver();
	}
	
	/**
	 * 
	 * @return DataMap: It provides the building Info data from Building Info File
	 */
	public Map<String, String> getBuildingInfo()
	{
		Map<String,String> temp = new HashMap<String, String>();
		
		for(Entry<Object,Object> entry: excel.getExcelData().get(ConfigUtil.getConfigUtil().getProperty(Constants.SHEET_NAME)).entrySet())
		{
			//System.out.println("Key: "+entry.getKey()+" Value: "+entry.getValue());
			temp.put((String)entry.getKey(), (String)entry.getValue());
		}
		
		return temp;
	}
	
	/**
	 * 
	 * @return DataMap: It provides the building Info data from Building Info File
	 */
	public Map<String, String> getThermalData()
	{
		Map<String,String> temp = new HashMap<String, String>();
		
		for(Entry<Object,Object> entry: excel.getExcelData().get(ConfigUtil.getConfigUtil().getProperty(Constants.THERMAL_SHEET)).entrySet())
		{
			System.out.println("Key: "+entry.getKey()+" Value: "+entry.getValue());
			String value = (String) entry.getValue();
			double dbl = Double.parseDouble(value);
			int val = (int)dbl;
			
			if(((String)entry.getKey()).contains("ThermalData"))
				temp.put((String)entry.getKey(), Integer.toString(val));
		}
		
		return temp;
	}
	
	/**
	 * 
	 * @return recessPeriodMap : Details about the recess period 
	 */
	public Map<String,String[]> getRecessPeriod()
	{
		Map<String,String[]> temp = new HashMap<String, String[]>();
		
		for(Entry<Object,Object> entry: excel.getExcelData().get(ConfigUtil.getConfigUtil().getProperty(Constants.HOLIDAY_SHEET)).entrySet())
		{
			temp.put((String)entry.getKey(), (String[])entry.getValue());
		}		
		return temp;
	}
	
	/**
	 * Returns DataProvider Data for the specified Excel Sheet
	 * @param Sheet_name
	 * @return String[][] (Data Provider Data)
	 */
	
	public String[][] getDataProviderData(String Sheet_name)
	{
		return excel.getDataProviderData().get(Sheet_name);
	}
	
	/**
	 * Returns last class test result
	 * @return Last execution TestResult
	 */
	
	public String getLastTestResult()
	{
		return listener.getLastTestResult();
	}
	
	/**
	 * Sets last test result
	 * @param lastTestResult
	 */
	public void setLastTestResult(String lastTestResult)
	{
		listener.setLastTestResult(lastTestResult);
	}
	
	/**
	 * Verifies the Earlier test results
	 * @return boolean value according to that
	 */
	public boolean verifyEarlierTestResult()
	{
		if(getLastTestResult().equalsIgnoreCase("Failed"))
			return false;
		
		return true;
	}
	
	/**
	 * 
	 * @return DisaggObjectRepository
	 */
	public Map<String, String> getDisaggObjectRepository()
	{
		Map<String,String> disaggMapping = new HashMap<String, String>();
		for(Entry<Object,Object> entry: excel.getExcelData().get(ConfigUtil.getConfigUtil().getProperty(Constants.Disaggregation_Sheet)).entrySet())
		{
			//System.out.println("Key: "+entry.getKey()+" Value: "+entry.getValue());
			disaggMapping.put((String)entry.getKey(), (String) entry.getValue());
		}
		return disaggMapping;
	}
}
