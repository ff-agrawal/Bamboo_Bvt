/**
 * Abstract Browser Class
 */
package com.framework.driverfactory;

import org.openqa.selenium.WebDriver;

/**
 * @author surendrane
 *
 */
public abstract class BrowserDriver {
	
	public abstract WebDriver createDriver();

}
