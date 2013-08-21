/**
 * Creates a Chrome WebDriver
 */
package com.framework.driverfactory;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class ChromeWebDriver extends BrowserDriver{

	WebDriver driver;
	
	@Override
	public WebDriver createDriver() {
		// TODO Auto-generated method stub
		
		try
		{
		//	System.setProperty("webdriver.chrome.driver", ConfigUtil.getConfigUtil().getProperty(Constants.CHROME_DRIVER_LOC));
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			//capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized", "--ignore-certificate-errors"));
			
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			
		}catch(Exception e)
		{
			System.out.println("Error while creating a Chrome WebDriver: "+e.getMessage());
		}
		return driver;
	}

}
