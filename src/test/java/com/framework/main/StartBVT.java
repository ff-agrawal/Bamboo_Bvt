/**
 * Main Class to execute the testNG XML
 */
package com.framework.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import com.framework.mail.SendMail;
import com.framework.updatedb.setupDB;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class StartBVT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Executing Tests in testng.xml");
		
		//Code to run the testng test suite
		try
		{
			List<String> xmlsuites = new ArrayList<String>(); 
			String bvt_result = "Passed";
			ArrayList<String> failure_classes = new ArrayList<String>();
			SendMail mail = new SendMail();
			setupDB setDB = new setupDB();
			
			File file = new File("testng.xml");
			xmlsuites.add(file.getAbsolutePath());
			TestNG testNg = new TestNG();
			testNg.setTestSuites(xmlsuites);

			//Adding TestListnerAdapter to track testng results - Suren
			
			TestListenerAdapter adapter = new TestListenerAdapter();
			testNg.addListener(adapter);
			testNg.setOutputDirectory(ConfigUtil.getConfigUtil().getProperty(Constants.BROWSER)+ "_output");
			testNg.run();
			System.out.println("All Test Cases ran successfully");
			
			List<ITestResult> failures = adapter.getFailedTests();
			
			if(failures.size() > 0)
			{
				bvt_result = "Failed";
				for(ITestResult result:failures)
					failure_classes.add(result.getMethod().getMethodName());	
			}
			
			/**
			 * Send mail to Firstfuel India
			 */
			
		//	mail.sendMail(bvt_result, failure_classes);			
		//	System.out.println("BVT Results have been Mailed");
			
			if(ConfigUtil.getConfigUtil().getProperty(Constants.UPDATE_TEST_LINK).equalsIgnoreCase("Yes"))
			{
				/**
				 * Updating Test link Database
				 */
				
				setDB.setUpTestLinkDataBase();
	            System.out.println("Test Link database has been updated successfully");
			}
				
		}catch(Exception e)
		{
			System.out.println("Error in testNg suite");
			System.out.println("Error :"+e.getMessage());
		}

	}

}
