/**
 * This class creates a new Firefox Driver
 */
package com.framework.driverfactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class FirefoxWebDriver extends BrowserDriver{
	
	WebDriver driver = null;
	
	@Override
	public WebDriver createDriver()
	{
		try
		{
			FirefoxProfile profile = new FirefoxProfile();
			profile.setAssumeUntrustedCertificateIssuer(false);
			
			File jserror = new File(ConfigUtil.getConfigUtil().getProperty(Constants.LIBS_FOLDER) + "JSErrorCollector.xpi");		
			profile.addExtension(jserror);
			
			if(ConfigUtil.getConfigUtil().getProperty(Constants.HAR_VIEW).equalsIgnoreCase("Yes"))
			{
				File firebug = new File(ConfigUtil.getConfigUtil().getProperty(Constants.LIBS_FOLDER)+"firebug-1.9.2-fx.xpi");
			    File netExport = new File(ConfigUtil.getConfigUtil().getProperty(Constants.LIBS_FOLDER)+ "netExport-0.8b22.xpi");
			    
				profile.addExtension(firebug);
	            profile.addExtension(netExport);
	            
	            // Set default Firefox preferences
		        profile.setPreference("app.update.enabled", false);

		        String domain = "extensions.firebug.";

		        // Set default Firebug preferences
		        profile.setPreference(domain + "currentVersion", "2.0");
		        profile.setPreference(domain + "allPagesActivation", "on");
		        profile.setPreference(domain + "defaultPanelName", "net");
		        profile.setPreference(domain + "net.enableSites", true);

		        // Set default NetExport preferences
		        profile.setPreference(domain + "netexport.alwaysEnableAutoExport", true);
		        profile.setPreference(domain + "netexport.showPreview", false);
		        profile.setPreference(domain + "netexport.defaultLogDir",ConfigUtil.getConfigUtil().getProperty(Constants.BASEPATH) );
		        
			}
			
			driver  = new FirefoxDriver(profile);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			
		}catch(IOException ioe)
		{
			System.out.println("The Extensions were not found : "+ioe.getMessage());
			
		}catch(Exception e)
		{
			System.out.println("Error while creating Firefox Driver: "+e.getMessage());
		}
		return driver;
	}

}
