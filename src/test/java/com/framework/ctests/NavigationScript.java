/**
 * 
 */
package com.framework.ctests;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.framework.library.BaseClass;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class NavigationScript extends BaseClass{
	
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	private Map<String,String> buildingInfo;
	String basepath = ConfigUtil.getConfigUtil().getProperty(Constants.BASEPATH);
	String building = "";
	
	@BeforeMethod
	public void setUp() throws Exception {	 
		
		buildingInfo = getBuildingInfo();
		driver = getWebDriver();
		baseUrl = ConfigUtil.getConfigUtil().getProperty(Constants.CONSUMER_URL);
	}
	
	@Test
	public void testNavigationScript() throws Exception
	{
		
		String ass_typ = (String)buildingInfo.get("ASSESMENT_TYPE");
		driver.get(baseUrl + "/session/new");
		
		consumerLogin(driver);
		driver.findElement(By.linkText("Get Started")).click();
		
		//Code to check URL Architecture
		//driver.findElement(By.linkText("Building Dashboard")).click();
		driver.get(baseUrl + "/buildings");
		
		waitForPageLoad(driver, 1000);
		
		building = ConfigUtil.getConfigUtil().getProperty(Constants.BUILDING_NAME);
		driver.findElement(By.xpath("//div[@id='buildings_dashboard_table_filter']/label/input")).sendKeys(building);
				
		driver.findElement(By.linkText(building)).click();
		waitForPageLoad(driver, 1000);
				
		LinkedHashMap<String,String> pageMap = new LinkedHashMap<String,String>();
		pageMap.put("Summary","link");
		pageMap.put("Whole Building Analysis", "link");
		pageMap.put("Weather Response", "link");
		pageMap.put("Annual Electric Demand Intensity", "link");
		pageMap.put("Monthly Consumption", "link");
		pageMap.put("daily_consumption_btn", "button");
		pageMap.put("Daily Electric Demand", "link");
		pageMap.put("seasonal_occupied_days_btn", "button");
		pageMap.put("peak_demand_days_btn", "button");
		pageMap.put("coldest_occupied_days_btn", "button");
		pageMap.put("hottest_occupied_days_btn", "button");
		pageMap.put("End Use Analysis", "link");
		pageMap.put("end_use_daily_btn", "button");
		pageMap.put("end_use_peak_demand_btn", "button");
		pageMap.put("Daily Electric Load Profile", "link");
		pageMap.put("Monitoring","link");
		pageMap.put("Daily Summary" ,"link");
		pageMap.put("Cumulative Summary", "link");
		pageMap.put("Recommendations", "link");
		//pageMap.put("Custom Analysis","link");
		
		for (Entry<String, String> entry : pageMap.entrySet())
		{
			String key = entry.getKey();
			if(key.equals("Weather Response") || key.equals("Recommendations") || key.equals("Custom Analysis"))
			{
				if(ass_typ.equalsIgnoreCase("RPA"))
					continue;
			}
			
			if(key.equals("Daily Summary") || key.equals("Cumulative Summary"))
			{
				if(driver.findElements(By.linkText("Daily Summary")).size() == 0)
					continue;			
			}
			
			if (entry.getValue().equalsIgnoreCase("link"))
				driver.findElement(By.linkText(entry.getKey())).click();
			else
				driver.findElement(By.id(entry.getKey())).click();
			
			waitForPageLoad(driver, 2000);
			/*
			 * Verifying charts in the page
			 */
			
			/*int size = 0;
			if(key.equals("Monthly Consumption") || key.equalsIgnoreCase("daily_consumption_btn"))
			{
				Map<String,String> ids = new HashMap<String, String>();
				ids.put("Monthly Consumption", "monthly_consumption");
				ids.put("daily_consumption_btn", "daily_consumption");
						
				size = driver.findElements(By.xpath("//div[@id='"+(String)ids.get(key)+"']")).size();
			}else
				size = driver.findElements(By.xpath("//div[substring(@id, string-length(@id) - 5) = '_chart']]")).size();
			
			if(size == 0)
			{
				captureScreenShot(driver, "ConsumerBVT_Failed.png", "Errors", building);
				Assert.fail("There were not charts found on: "+key);
			}*/
				
		}
	}
			
	/**
	 * @throws java.lang.Exception
	 */
	
	@AfterMethod
	public void tearDown() throws Exception {
		
		building = ConfigUtil.getConfigUtil().getProperty(Constants.BUILDING_NAME);
		
		if(ConfigUtil.getConfigUtil().getProperty(Constants.BROWSER).equalsIgnoreCase("Firefox"))
		{
			createFolder(basepath + building);
			createFolder(basepath + building + "\\JSErrors");
			
			String filename = basepath + building + "\\JSErrors\\JSErrors.txt";
			//findJavaScriptErrors(filename, driver);
		}
		
		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {			
			Assert.fail(verificationErrorString);
		}
	}

	/**
	 * Creates the folder if its not present
	 * @param folder_path
	 */
	public void createFolder(String folder_path)
	{
		File file = new File(folder_path);
		
		if(!file.exists())
			file.mkdir();
	}

}
