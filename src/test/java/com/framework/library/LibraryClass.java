/**
 * This Class contains Library functions
 */
package com.framework.library;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

//import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.framework.dataobjects.TestListener;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class LibraryClass {
	
	/**
	 * This function is used for wait until all the ajax requests in a page are completed
	 * @param driver
	 * @param ms
	 */
	
	public void waitForPageLoad(WebDriver driver,long ms)
	{
		try
		{
			for (int second = 0;; second++) 
			{
				if (second >= 120) Assert.fail("Timeout: Ajax calls are not getting completed");
		    	JavascriptExecutor js = (JavascriptExecutor) driver;
		    	String script = "return jQuery.active <= 1;";
		    	//Testing purpose 
		    	/*Object no_of_pending_calls = (Object) js.executeScript("return jQuery.active");
		    	System.out.println(no_of_pending_calls);*/
		        boolean noOfAjaxCallsPending =  (Boolean) js.executeScript(script);
		        
		        if (noOfAjaxCallsPending)
		        {
		        	Thread.sleep(ms);
		            break;
		        }
		        
		        Thread.sleep(3000);
		    }
		}catch(Exception e){
			System.out.println("Error: "+e.getMessage());
		}
	}
	
	/**
	 * This library function would enable user to login the firstfuel application
	 * @param driver
	 */
	public void applicationLogin(WebDriver driver)
	{
		try
		{
			System.out.println("Into Login");
			System.out.println("Login Page URL: "+driver.getCurrentUrl());
			driver.findElement(By.id("login_name")).clear();
			System.out.println("User Name :"+ConfigUtil.getConfigUtil().getProperty(Constants.USERNAME));
			driver.findElement(By.id("login_name")).sendKeys(ConfigUtil.getConfigUtil().getProperty(Constants.USERNAME));
			driver.findElement(By.id("password")).clear();
			System.out.println("Password :"+ConfigUtil.getConfigUtil().getProperty(Constants.PASSWORD));
			driver.findElement(By.id("password")).sendKeys(ConfigUtil.getConfigUtil().getProperty(Constants.PASSWORD));
			driver.findElement(By.name("commit")).click();
			sleep(5000);
			Assert.assertTrue(driver.findElement(By.id("tenant_id")).isDisplayed());
			System.out.println("Login Success");
			
			System.out.println("Into Tenant selection");
			System.out.println("Home Page URL: "+driver.getCurrentUrl());
			new Select(driver.findElement(By.id("tenant_id"))).selectByVisibleText(ConfigUtil.getConfigUtil().getProperty(Constants.TENANT_NAME));
			sleep(5000);
			System.out.println("Tenanat Selection Success");
			// Temporary code 
			
			if( driver.getPageSource().contains("First_audit")) {
				System.out.println("Inside if");
				//driver.findElement(By.id("bd-tab2")).click();
				
				//driver.findElement(By.xpath("//li[@id='bd-tab2']/a/img")).click();
				driver.findElement(By.cssSelector("[alt='First_audit']")).click();
				System.out.println("select first audit");
				sleep(7000);
			}
		}catch(Exception e)
		{
			System.out.println("Error while logging into the application "+e.getMessage());
			Assert.fail("Error while logging into the application");
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
	public void consumerLogin(WebDriver driver)
	{
		try
		{
			driver.findElement(By.id("login_name")).clear();
			driver.findElement(By.id("login_name")).sendKeys(ConfigUtil.getConfigUtil().getProperty(Constants.CONSUMER_USERNAME));
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(ConfigUtil.getConfigUtil().getProperty(Constants.CONSUMER_PASSWORD));
			driver.findElement(By.name("commit")).click();
			waitForPageLoad(driver,2000);
				
		}catch(Exception e)
		{
			System.out.println("Error while logging into the application "+e.getMessage());
			Assert.fail("Error while logging into the application");
		}
	}
	/**
	 * This function is used for opening the building
	 * @param driver
	 * @param bld_Name
	 */
	public void openProcessConsole(WebDriver driver,String bld_Name)
	{
		try
		{
			/*
			 * Opening process console of a Building
			 */
				
			WebElement element  = driver.findElement(By.id("search_buildings"));
			if(element.isDisplayed())
			{			
				element.clear();
				waitForPageLoad(driver, 1000);
				//element.sendKeys("BVT -");
				element.sendKeys(bld_Name);
				waitForPageLoad(driver, 2000);
				driver.findElement(By.linkText(bld_Name)).click();
			}
		}catch(Exception e)
		{
			System.out.println("Building specified was not found "+e.getMessage());
			Assert.fail("Building specified was not found");
		}
	}
	
	/**
	 * This function is too find any javascript errors in a page 
	 * using JSError Interface
	 */
/*	public void findJavaScriptErrors(String filename, WebDriver driver)
	{
		List<JavaScriptError> jsErrors = JavaScriptError.readErrors(driver);
		FileWriter fstream;
		BufferedWriter out ;
		try {
			fstream = new FileWriter(filename);
			out = new BufferedWriter(fstream);
			
			out.write("###JavaScript Errors");
			out.newLine();
	        for(int i = 0; i < jsErrors.size(); i++) {
	            out.write(jsErrors.get(i).getErrorMessage() + "\t");
	            out.write(jsErrors.get(i).getLineNumber() + "\t");
	            out.write(jsErrors.get(i).getSourceName());
	            out.newLine();
	        }			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("JavaScript error file not Created: "+e.getMessage());
		}catch (IOException ioe)
		{
			System.out.println("Javascript error file not present: "+ioe.getMessage());
		}   
	}*/
	
	/**
	 * This method checks the status of the provided link 
	 * @param driver
	 * @param link_String
	 * @return
	 */

	public boolean checkBuildingStatus(WebDriver driver, String link_String)
	{
		String id = "";
		
		if(link_String.equalsIgnoreCase("QA"))
			id = "qa-console";
		else
			id = driver.findElement(By.linkText(link_String)).getAttribute("id");
		
		String status = driver.findElement(By.xpath("//a[@id='"+id+"']/i")).getAttribute("class");
		if(status.equalsIgnoreCase("pass") || status.equalsIgnoreCase("warning") || status.equalsIgnoreCase("in-progress")|| status.equalsIgnoreCase("completed"))
			return true;
		else
			return false;
	}
	
	public String checkQAStatus(WebDriver driver)
	{
		String status = driver.findElement(By.xpath("//a[@id='qa-console']/i")).getAttribute("class");	
		return status;
	}
	
	/**
	 * This method is used for adding recommendation
	 * @param driver
	 * @param name
	 * @param description
	 * @param type
	 * @param impact
	 */
	public void addRecommendation(WebDriver driver,String name, String description,String type, String impact)
	{	
		driver.findElement(By.id("add_recommendation")).click();
		waitForPageLoad(driver,1000);
		driver.findElement(By.id("building_recommendation_recommendation")).clear();
		waitForPageLoad(driver,1000);
		driver.findElement(By.id("building_recommendation_recommendation")).sendKeys(name);
		waitForPageLoad(driver,1000);
		driver.findElement(By.id("building_recommendation_description")).clear();
		waitForPageLoad(driver,1000);
		driver.findElement(By.id("building_recommendation_description")).sendKeys(description);
		waitForPageLoad(driver,1000);
		new Select(driver.findElement(By.id("building_recommendation_recommendation_type_id"))).selectByVisibleText(type);
		waitForPageLoad(driver,1000);
		new Select(driver.findElement(By.id("building_recommendation_energy_impact_id"))).selectByVisibleText(impact);
		driver.findElement(By.id("total_saving_1")).clear();
		waitForPageLoad(driver,1000);
		driver.findElement(By.id("total_saving_1")).sendKeys("12345");
		
		waitForPageLoad(driver,1000);
		driver.findElement(By.cssSelector("input[value=\"Add Recommendation\"]")).click();	
		waitForPageLoad(driver,3000);
	}
		
	/**
	 * This method is used for removing/deleting the specified recommendation
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void deleteRecommendation(WebDriver driver, String name) throws InterruptedException
	{
		try
		{
		List<WebElement> existingRecommendation = driver.findElements(By.xpath("//div[@id='recommendation-rows']/div[starts-with(@id,'recommendation-')]"));
		for (WebElement row : existingRecommendation)
		{	
			if(row.getText().contains(name))
			{
				String id = row.getAttribute("id").split("recommendation-")[1];
				row.findElement(By.xpath("//td[@id='more-options-menu-"+id+"']/div[@class='dropdown floatR googleUI']/a")).click();
				
				row.findElement(By.id("delete-recommendation-"+id)).click();
				waitForPageLoad(driver,3000);
				driver.findElement(By.xpath("//button[@type='button']")).click();	
				
				break;
			}
		}
		driver.findElement(By.linkText("Recommendations")).click();
		Thread.sleep(2000);
		}catch(Exception e)
		{
			System.out.println("Error "+e.getMessage() );
		}
	}
	
	/**
	 * This Method is used for capturing the screen and save it as a image
	 * @param driver
	 * @param filename
	 */
	public void captureScreenShot(WebDriver driver,String filename,String folder_Name)
	{
		String path = ConfigUtil.getConfigUtil().getProperty(Constants.BASEPATH);
		try 
		{	
			File building_folder = new File(path + "\\"+ TestListener.getInstance().getBuildingInfo().get("BUILDING_NAME"));
			if(!building_folder.exists())
				building_folder.mkdir();
			
			File folder = new File(building_folder.getAbsoluteFile()+ "\\"+ folder_Name);
			if(!folder.exists())
				folder.mkdir();
			
			driver.findElement(By.id("footer-inner")).click();
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(folder.getAbsolutePath() + "\\"+ filename));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error occured while capturing Screen shot: "+e.getMessage());
		}
	}
	
	/**
	 * Function to read data from json object to HashMap
	 * @return HashMap<String,Object>
	 */
	public HashMap<String,Object> jsonToHash(String jsonFile) 
	{
		HashMap<String,Object> jsonParamerters = null;
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			File from = new File(jsonFile); 
			TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
			jsonParamerters = mapper.readValue(from, typeRef);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to parse the JSON file: "+e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to parse the JSON file: "+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to read the JSON file: "+e.getMessage());
		} 
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return jsonParamerters;
	}
}
