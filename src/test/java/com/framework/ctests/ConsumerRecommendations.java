/**
 * 
 */
package com.framework.ctests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.framework.library.BaseClass;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class ConsumerRecommendations extends BaseClass{
	
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	
	@BeforeClass
	public void setUp() throws Exception {	 
		
		try
		{
			driver = getWebDriver();
			baseUrl = ConfigUtil.getConfigUtil().getProperty(Constants.CONSUMER_URL);
			driver.get(baseUrl + "/session/new");	
			consumerLogin(driver);
			
			driver.findElement(By.linkText("Building Dashboard")).click();
			waitForPageLoad(driver, 1000);
			
			String building = ConfigUtil.getConfigUtil().getProperty(Constants.BUILDING_NAME);
			driver.findElement(By.xpath("//div[@id='buildings_dashboard_table_filter']/label/input")).sendKeys(building);
			
			driver.findElement(By.linkText(building)).click();
			waitForPageLoad(driver, 1000);
			
			driver.findElement(By.linkText("Recommendations")).click();
			waitForPageLoad(driver, 1000);
			
		}catch(NoSuchWindowException nse)
		{
			driver.quit();
		}
	}
	
	@Test(dataProvider = "DP1")
	public void testConsumerAddRecommendation(String name, String description,String type, String impact) throws Exception {
		addRecommendation(driver, name, description, type, impact);
		
		//String building = ConfigUtil.getConfigUtil().getProperty(Constants.BUILDING_NAME);
		if (!verifyElements(name)) {
			captureScreenShot(driver, "ConsumerBVT_Failed.png", "Errors");
			setLastTestResult("Failed");
			AssertJUnit.fail("Recommendation " + name + "not found");
		}
	}
	
	@Test(dataProvider = "DP1")
	public void testConsumerDeleteRecommendation(String name, String description,String type, String impact) throws Exception {		
		
		deleteRecommendation(driver, name);
		//String building = ConfigUtil.getConfigUtil().getProperty(Constants.BUILDING_NAME);
		if (verifyElements(name)) {
			captureScreenShot(driver, "Consumer_BVT_Failed.png", "Errors");
			setLastTestResult("Failed");
			AssertJUnit.fail("Recommendation " + name + "found");
		}		
	}
	
	@DataProvider(name = "DP1")
	public String[][] getData() throws Exception {
		return getDataProviderData(ConfigUtil.getConfigUtil().getProperty(Constants.RECOMMENDATION_SHEET));
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	protected boolean verifyElements(String name) 
	{
		boolean isPresent = false;

		driver.findElement(By.linkText("Recommendations")).click();
		waitForPageLoad(driver,2000);
		List<WebElement> getRecom = driver.findElements(By.xpath("//tr[starts-with(@id,'recommendation_')]"));
		for (WebElement row : getRecom) 
		{
				if(row.getText().contains(name))
				{
					isPresent = true;
					break;
				}
		}
			return isPresent;
	}
	
	/**
	 * @throws java.lang.Exception
	 */


	@AfterClass
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			AssertJUnit.fail(verificationErrorString);
		}
	}


}
