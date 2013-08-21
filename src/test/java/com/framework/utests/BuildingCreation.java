/**
 * Project: BVT Automation
 * Feature: Create Building Scenario
 */
package com.framework.utests;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.framework.dataobjects.TestListener;
import com.framework.library.BaseClass;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * @author surendrane
 *
 */
public class BuildingCreation extends BaseClass{
	
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	private Map<String,String> buildingInfo;
		
	@BeforeMethod
	public void setUp() throws Exception {	  
		
		buildingInfo = getBuildingInfo();
		driver = getWebDriver();
		baseUrl = ConfigUtil.getConfigUtil().getProperty(Constants.APPLICATION_URL);
	}
	
	@Test
	public void testBuildingCreation() throws Exception
	{
		try
		{
			driver.get(baseUrl + "/session/new");	
			applicationLogin(driver);
			
			System.out.println("Into building creation");
			driver.findElement(By.linkText("New")).click();
			sleep(2000);
			driver.findElement(By.linkText("Add New Building")).click();
			waitForPageLoad(driver,2000);
			
			//Appending Date to Building Name - Setting globally
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm:ss");			
			String buildingName = (String)buildingInfo.get("BUILDING_NAME") + " " +dateFormat.format(date);
			
			TestListener.getInstance().setBuildingName(buildingName);
			System.out.println("Building Name = "+buildingName);
			driver.findElement(By.id("building_name")).sendKeys(buildingName);
			sleep(1000);
			driver.findElement(By.id("building_label")).sendKeys((String)buildingInfo.get("BUILDING_NAME"));
			sleep(1000);
			driver.findElement(By.id("building_address_attributes_address1")).sendKeys((String)buildingInfo.get("ADDRESS_1"));
			sleep(1000);
			new Select(driver.findElement(By.id("building_address_attributes_country"))).selectByVisibleText((String)buildingInfo.get("COUNTRY"));
			sleep(1000);
			driver.findElement(By.id("building_address_attributes_zip")).sendKeys((String)buildingInfo.get("ZIPCODE"));
			sleep(3000);
			//driver.findElement(By.id("building_address_attributes_city")).click();
			new Select(driver.findElement(By.id("building_address_attributes_state_id"))).selectByVisibleText((String)buildingInfo.get("STATE"));
			sleep(1000);
			/*driver.findElement(By.id("building_address_attributes_city")).sendKeys((String)buildingInfo.get("CITY"));
			sleep(1000);*/
			
			driver.findElement(By.id("building_submit")).click();
			System.out.println("Building Creation 1st page Successfull");
			sleep(8000); 
	
			//Checking without Sikuli -- Using Send keys 
			
			/*driver.findElement(By.linkText("Building Pictures")).click();
			sleep(1000);
			driver.findElement(By.id("building_picture_image")).sendKeys(ConfigUtil.getConfigUtil().getProperty(Constants.BUILDING_IMAGES));
			sleep(1000);
			
			driver.findElement(By.name("commit")).click();
			waitForPageLoad(driver,3000);*/
			
			/**
			 * Code to check Image upload
			 */
			
			/*if(!(driver.findElements(By.xpath("//a[starts-with(@id,'building_picture_')]")).size() > 0))
				Assert.fail("Building Image cannot be uploaded");*/
			
			driver.findElement(By.linkText("Building Construction")).click();
			sleep(1000);
			driver.findElement(By.id("building_area_gsf")).sendKeys((String)buildingInfo.get("Gross Square Ft (Total)"));
			sleep(1000);
			driver.findElement(By.id("building_submit")).click();
			waitForPageLoad(driver,3000);
			
			
			driver.findElement(By.linkText("Use and Occupancy")).click();
			sleep(1000);
			new Select(driver.findElement(By.id("building_usage_primary_activity_id"))).selectByVisibleText((String)buildingInfo.get("TYPE (Office, School, Library etc)"));
			sleep(1000);
			driver.findElement(By.id("building_submit")).click();
			waitForPageLoad(driver,4000);
			
			driver.findElement(By.id("avg_occupancy")).sendKeys("100");
			sleep(1000);
			driver.findElement(By.id("building_submit")).click();
			waitForPageLoad(driver,4000);
			
			new Select(driver.findElement(By.id("building_energy_source_primary_heating"))).selectByVisibleText((String)buildingInfo.get("Primary Heating"));
			sleep(1000);
			new Select(driver.findElement(By.id("building_energy_source_primary_cooling"))).selectByVisibleText((String)buildingInfo.get("Primary Cooling"));
			sleep(1000);
			driver.findElement(By.id("building_submit_exit")).click();
			waitForPageLoad(driver,5000);
			
			
			//Code for confirmation of building 		
			openProcessConsole(driver, TestListener.getInstance().getBuildingName());
			sleep(5000);
			if(!(checkBuildingStatus(driver, "Building Information")))
			{
				captureScreenShot(driver, "BVT_Failed.png", "Errors");
				setLastTestResult("Failed");
				Assert.fail("The building information was not updated properly");
			}
		}catch(Exception e)
		{
			captureScreenShot(driver, "BVT_Failed.png", "Errors");
			setLastTestResult("Failed");
			Assert.fail("Building Creation Failed\n"+e.getMessage());
		}
		
	}
	private void sleep(long arg0) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * @throws java.lang.Exception
	 */

	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}
	

}
