/**
 * This class creates a IEDriver
 */
package com.framework.driverfactory;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class IEWebDriver extends BrowserDriver{

	WebDriver driver;
	@Override
	public WebDriver createDriver() {
		// TODO Auto-generated method stub
		try
		{
			System.setProperty("webdriver.ie.driver", ConfigUtil.getConfigUtil().getProperty(Constants.IE_DRIVER_LOC));
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
	
			driver = new InternetExplorerDriver(ieCapabilities);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);			
			
		}catch(Exception e)
		{
			System.out.println("Error occured while creating a IEDriver: "+e.getMessage());
		}
		return driver;
	}
	
	

}
