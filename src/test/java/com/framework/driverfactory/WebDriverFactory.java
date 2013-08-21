/**
 * Depending upon the configuration - WebDriver is selected
 */
package com.framework.driverfactory;

import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class WebDriverFactory{
	
	BrowserDriver browser = null;
	
	public BrowserDriver getBrowser()
	{
		String browser_type = ConfigUtil.getConfigUtil().getProperty(Constants.BROWSER);
		System.out.println("browser type:"+browser_type);
		if(browser_type.equalsIgnoreCase("Firefox"))
			return new FirefoxWebDriver();
		else if(browser_type.equalsIgnoreCase("Headless"))
			return new HeadlessWebDriver();
		else if(browser_type.equalsIgnoreCase("Chrome"))
			return new ChromeWebDriver();
		else
			return new IEWebDriver();
		
	}

}
