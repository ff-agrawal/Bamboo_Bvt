package com.framework.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigUtil {

  private static ConfigUtil configObj = null;

  private static Properties configdata = null;

  static {
    if (configObj == null) {
      configObj = new ConfigUtil();
    }
  }

  private ConfigUtil() {
    configdata = new Properties();
    try {
    	configdata.load(new FileInputStream(System.getProperty("user.dir")+"//target//test-classes//config.properties"));
    	//configdata.load(ClassLoader.getSystemResourceAsStream("config.properties"));
    }
    catch (IOException e) {
      System.out.println("Error in reading the properties file  : " + e);
    }
  }

  /**
   * Returns the config Util Object
   *
   * @return
   */
  public static ConfigUtil getConfigUtil() {
    if (configObj != null) {
      return configObj;
    }
    else {
      return null;
    }
  }

  /**
   * Return the value of the property in the properties file
   *
   * @param name
   * @return
   */
  public String getProperty(String name) {
    if (name != null) {
      String value = configdata.getProperty(name);
      if (value == null)
        return "";
      else
        return value;
    }
    else {
      return null;
    }
  }

  /**
   * Returns property object.
   *
   * @return
   */
  public static Properties getProperty() {
    if (configdata != null) {
      return configdata;
    }
    else {
      return null;
    }
  }
}


