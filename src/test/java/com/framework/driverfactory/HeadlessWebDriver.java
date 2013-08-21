/**
 * Provides GhostDriver instance 
 */
package com.framework.driverfactory;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author surendrane
 *
 */
public class HeadlessWebDriver extends BrowserDriver{
	
	WebDriver driver; 
	
	@Override
	public WebDriver createDriver()
	{
		try
		{
			Capabilities caps = new DesiredCapabilities();
			
	       /* ((DesiredCapabilities) caps).setCapability("takesScreenshot", true); 
	        ((DesiredCapabilities) caps).setCapability("ignore-ssl-errors", "yes");
	        ((DesiredCapabilities) caps).setCapability(
	            PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
	            "D:/DevSoftwares/phantomjs-1.9.1-windows/phantomjs.exe"
	        );*/
			((DesiredCapabilities) caps).setCapability("takesScreenshot", true); 
	        ((DesiredCapabilities) caps).setCapability("ignore-ssl-errors", "yes");
	        driver = new PhantomJSDriver(caps);
	        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	        
		}catch(Exception e)
		{
			System.out.println("Error while creating a Headless WebDriver: "+e.getMessage());
		}
		
		
		return driver;
	}
	

}
