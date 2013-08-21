
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
 * @author Ankit
 *
 */
public class LoginPass extends BaseClass{
	
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
	public void testLogin() throws Exception
	{
		try
		{
			driver.get(baseUrl + "/session/new");	
			System.out.println("Into Login");
			System.out.println("Login Page URL: "+driver.getCurrentUrl());
			driver.findElement(By.id("login_name")).clear();
			System.out.println("User Name :"+ConfigUtil.getConfigUtil().getProperty(Constants.USERNAME));
			driver.findElement(By.id("login_name")).sendKeys(ConfigUtil.getConfigUtil().getProperty(Constants.USERNAME));
			driver.findElement(By.id("password")).clear();
			System.out.println("Password :"+ConfigUtil.getConfigUtil().getProperty(Constants.PASSWORD));
			driver.findElement(By.id("password")).sendKeys(ConfigUtil.getConfigUtil().getProperty(Constants.PASSWORD));
			driver.findElement(By.name("commit")).click();
			Thread.sleep(7000);
			Assert.assertTrue(driver.findElement(By.id("tenant_id")).isDisplayed());
			System.out.println("Tenant id is displayed"+driver.findElement(By.id("tenant_id")).isDisplayed());
			System.out.println("Login Success");
			
			System.out.println("Logging out the session");
			driver.findElement(By.linkText("Logout")).click();
			System.out.println("Successfully logged out of the application");
		}catch(Exception e)
		{
			
			Assert.fail("Application Login failed\n"+e.getMessage());
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
