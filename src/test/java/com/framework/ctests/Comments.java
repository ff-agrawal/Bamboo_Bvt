package com.framework.ctests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.AssertJUnit;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.library.BaseClass;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */

public class Comments extends BaseClass{

	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	String building = "";

	@BeforeMethod
	public void setUp() throws Exception {	
		
		driver = getWebDriver();
		baseUrl = ConfigUtil.getConfigUtil().getProperty(Constants.CONSUMER_URL);
		building = ConfigUtil.getConfigUtil().getProperty(Constants.BUILDING_NAME);
	}
	
	@Test
	public void testComments() throws Exception
	{
		try
		{
			boolean flag=false;
			driver.get(baseUrl + "/session/new");
			
			consumerLogin(driver);
			driver.findElement(By.linkText("Building Dashboard")).click();
			waitForPageLoad(driver, 1000);
		
			driver.findElement(By.xpath("//div[@id='buildings_dashboard_table_filter']/label/input")).sendKeys(building);
			
			driver.findElement(By.linkText(building)).click();
			waitForPageLoad(driver, 1000);
			
			driver.findElement(By.linkText("Comments")).click();
			driver.findElement(By.linkText("Add Comment")).click();
			waitForPageLoad(driver,2000);
			driver.findElement(By.id("analysis_topic_title")).clear();
			waitForPageLoad(driver,1000);
			driver.findElement(By.id("analysis_topic_title")).sendKeys("CommentThroughAutomation");
			waitForPageLoad(driver,1000);
			driver.findElement(By.id("analysis_topic_description")).clear();
			waitForPageLoad(driver,1000);
			driver.findElement(By.id("analysis_topic_description")).sendKeys("Commenting it through automation");
			waitForPageLoad(driver,1000);
			driver.findElement(By.xpath("//input[@name='commit']")).click();
			waitForPageLoad(driver,2000);
			
			/*
			 * Verifying the added comments
			 */
			
			String delete_id = "";
			List<WebElement> inputs = driver.findElements(By.xpath("//span[starts-with(@id, 'topic_')]"));
			 Iterator<WebElement> iterator = inputs.iterator();
			 
			    while (iterator.hasNext()) {
			    	WebElement header = iterator.next();
			    	if(header.getText().equals("CommentThroughAutomation"))
			    	{
			    		List<WebElement> list = driver.findElements(By.xpath("//div[starts-with(@id, 'topic_')]"));
			   		 	Iterator<WebElement> itr = list.iterator();
			   		 	
			   		 	while (itr.hasNext())
			   		 	{
			   		 		WebElement comments = itr.next();	   		 		
				   		 	if (comments.getText().equals("Commenting it through automation"))
				   		 	{
					    		flag=true;
					    		delete_id = comments.getAttribute("id");
					    		delete_id = delete_id.split("_")[1];
					    	}
			   		 	}
			    	}
			    	
			    }
			    
			    if (flag==false) {
			    	captureScreenShot(driver, "BVT_Failed.png", "Errors");
					setLastTestResult("Failed");
					Assert.fail("Add Comments failed");
				}else
				{
					//Delete the comment - Suren
					driver.findElement(By.id("delete_"+delete_id)).click();
					driver.findElement(By.xpath("//span[@class='ui-button-text']")).click();
					waitForPageLoad(driver, 1000);
					
					if(driver.findElements(By.id("topic_"+delete_id)).size() > 0)
					{
						captureScreenShot(driver, "BVT_Failed.png", "Errors");
						setLastTestResult("Failed");
						Assert.fail("Delete Comments failed");
					}
				}
		}catch(Exception e)
		{
			captureScreenShot(driver, "BVT_Failed.png", "Errors");
			Assert.fail("Add Comments Failed\n"+e.getMessage());
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
			AssertJUnit.fail(verificationErrorString);
		}
	}
}


	
	