/**
 * This class is would set the last executed TestNG result
 */
package com.framework.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.framework.singleton.ExcelDataInMaps;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class TestListener {
	
	private static TestListener instance;
	private String lastTestResult = "Passed";
	private String buildingName = "";
	Map<String,String> buildingInfo;
	ExcelDataInMaps excel = ExcelDataInMaps.getInstance();
		
	public static synchronized TestListener getInstance()
	{
		if (instance == null)
		  {
			instance = new TestListener();
		  }
		  return instance;
	}

	/**
	 * @return the lastTestResult
	 */
	public String getLastTestResult() {
		return lastTestResult;
	}

	/**
	 * @param lastTestResult the lastTestResult to set
	 */
	public void setLastTestResult(String lastTestResult) {
		this.lastTestResult = lastTestResult;
	}
	
	/**
	 * @return the buildingName
	 */
	public String getBuildingName() {
		System.out.println(buildingName);
		if(buildingName == "")
		{
			buildingInfo = getBuildingInfo();
			return (String)buildingInfo.get("BUILDING_NAME");
		}
		else
			return buildingName;
	}

	/**
	 * @param buildingName the buildingName to set
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	

	
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
	
}
