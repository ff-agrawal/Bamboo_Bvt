/**
 * Sending mail to the required users 
 */
package com.framework.mail;
import java.io.File;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import com.framework.dataobjects.TestListener;
import com.framework.util.ConfigUtil;
import com.framework.util.Constants;

/**
 * @author surendrane
 *
 */
public class SendMail{
	
	  public void sendMail(String Status, ArrayList<String> failedTests) {
		  
		  final String username = "bvt@firstfuel.com";
		  final String password = "r0@d2w1n";

		  String release_info = ConfigUtil.getConfigUtil().getProperty(Constants.RELEASE_INFO);
		  ConfigUtil configUtil = ConfigUtil.getConfigUtil();

		  // SUBSTITUTE YOUR EMAIL ADDRESSES HERE
		  
	        String to = ConfigUtil.getConfigUtil().getProperty(Constants.EMAIL_TO_ADDRESS);
	        //String cc = "surendran@firstfuel.com";
	        String from = "bvt@firstfuel.com";
	        
	        //teamindia@firstfuel.com
	        
	        Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			
			// To see what is going on behind the scene
	        //props.put("mail.debug", "true");
	        
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					  });

	        try 
	        {	        	
	        	// Instantiate a message
	        	
	            Message msg = new MimeMessage(session);
	
	            //Set message attributes
	            msg.setFrom(new InternetAddress(from));
	            InternetAddress[] address = {new InternetAddress(to)};
	            //InternetAddress[] cc_address = {new InternetAddress(cc)};
	            msg.setRecipients(Message.RecipientType.TO, address);
	            //msg.addRecipients(Message.RecipientType.CC, cc_address);
	            msg.setSubject(release_info + " Automated BVT Report: Status: "+Status);
	            msg.setSentDate(new Date());
	            
	            Multipart multipart = new MimeMultipart();
	            BodyPart part1 = new MimeBodyPart();
	            String content = "";
	            
	            content += "Hi All,";
	            content += "\n\nThe Automated BVT got "+Status+" for the latest build on "+ release_info + 
        		"\n\nAttached is the BVT result generated through TestNG.\nBelow mentioned are the details of the building created and performed through BVT has been performed:" +
        		"\n\n1. Tenant Name: " + configUtil.getProperty(Constants.TENANT_NAME)+
        		"\n2. URL: "+configUtil.getProperty(Constants.APPLICATION_URL)+ 
        		"\n3. Building Name: "+TestListener.getInstance().getBuildingName()+
	            "\n4. Browser: "+configUtil.getProperty(Constants.BROWSER);
	            
	            if(Status.equalsIgnoreCase("Failed"))
	            {
	            	content += "\n\nFailed Tests:\n";
	            	for(String methodName: failedTests)
	            		content += "\n"+methodName;
	            }
	            
	            content += "\n\n\nRegards\nQA Team";
	            part1.setText(content);
	            
	            multipart.addBodyPart(part1);
	            
	            //Adding BVT Results produced by TestNG
	            
	            String resultFilePath = ".\\"+configUtil.getProperty(Constants.BROWSER)+ "_output"+"\\emailable-report.html";
	            
	            BodyPart attachment = addAttachment(resultFilePath, "BVT_"+configUtil.getProperty(Constants.BROWSER)+"_Results.html");
	            multipart.addBodyPart(attachment);
	            
	            //Adding Error Screenshot for failure scenario
	            try
	            {
		            if(Status.equalsIgnoreCase("Failed"))
		            {           	
		            	String errorFilePath = configUtil.getProperty(Constants.BASEPATH) + TestListener.getInstance().getBuildingName()+
						 "\\Errors\\BVT_Failed.png" ;
	
		            	BodyPart attachment1 = addAttachment(errorFilePath, "Failure_Screenshot.png");
		            	if(attachment1 != null)
		            		multipart.addBodyPart(attachment1);
		            }
	            
		            String dataQAChecks = configUtil.getProperty(Constants.BASEPATH) + TestListener.getInstance().getBuildingName()+ "\\DataQA_Checks_results.txt";
		            String analysisQAChecks = configUtil.getProperty(Constants.BASEPATH) + TestListener.getInstance().getBuildingName()+ "\\AnalysisQA_Checks_results.txt";
		            
	           
		            BodyPart attachment2 = addAttachment(dataQAChecks, "DataQA_Checks");
		            if(attachment2 != null)
		            	multipart.addBodyPart(attachment2);
		            
		            BodyPart attachment3 = addAttachment(analysisQAChecks,"AnalysisQA_Checks");
		            if(attachment3 != null)
		            	multipart.addBodyPart(attachment3);
		            
	            }catch(Exception e)
	            {
	            	System.out.println("Error while adding attachment "+e.getMessage());
	            }
	            
	            msg.setContent(multipart);
	            //Send the message
	            Transport.send(msg);
	        }
	        catch(SendFailedException sfe)
	        {
	        	System.out.println("Error while sending mail: "+sfe.getMessage());
	        }
	        catch (Exception e) {
	            // Prints all nested (chained) exceptions as well
	        	System.out.println("Error sending mail: "+e.getMessage());
	        }
	  }
	  
	  public BodyPart addAttachment(String filePath,String filename)
	  {
		  BodyPart attachment = new MimeBodyPart();
		  try
		  {		  
			  File file = new File(filePath);
			  if(!file.exists())
				  return null;
			  DataSource source = new FileDataSource(file);
	      	  attachment.setDataHandler(new DataHandler(source));
	      	  attachment.setFileName(filename);
		  }catch(Exception mse)
		  {
			  System.out.println("Error while adding attachment "+mse.getMessage());
		  }
		  return attachment;
	  }
}
