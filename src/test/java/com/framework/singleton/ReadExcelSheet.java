package com.framework.singleton;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.framework.util.ConfigUtil;

public class ReadExcelSheet{
	
	ConfigUtil configData = ConfigUtil.getConfigUtil();
	RegisterSheetsInExcel registry = RegisterSheetsInExcel.getInstance();
	
	/**
	 * 
	 * @param sheet_name
	 * @return Data present in the specified registered sheet
	 */
	public Map<Object,Object> getInfoFromExcelSheet(String sheet_name)
	 {
	     int endRow = 0;
	     int endColumn = 0;
	     InputStream iStream = null;
		 XSSFWorkbook workBook = null;
		 Map<Object, Object> dataMap = new HashMap<Object,Object>();
		 
		 try
		 {
			 iStream =  new FileInputStream(registry.getRegisteredSheets().get(sheet_name));
			 workBook = new XSSFWorkbook(iStream);
			 iStream.close();
			 
			 if(workBook != null)
			 {
				 XSSFSheet sheet = workBook.getSheet(sheet_name);
				 
				 if(sheet != null)
				 {
					 endRow = sheet.getLastRowNum();
					 for (int i = 0; i <= endRow; i++) 
			    	  {
			    	        XSSFRow row = sheet.getRow(i);

			    	        if (row != null && (row.getCell(0) != null)) 
			    	        {
			    	        	endColumn = row.getLastCellNum();
			    	        	if(endColumn > 1)
			    	        	{
			    	        		String key = "";
			    	        		ArrayList<String> list = new ArrayList<String>();
			    	       		 	int val =0;
			    	       		 	
			    	        		for(int r=1;r<endColumn;r++)
			    	        		{
			    	        			if(row.getCell(r) != null)
			    	        			{
			    	        				String tmp = (row.getCell(r)).toString();
			    	        				if(tmp !=null && tmp !="")
			    	        				{
			    	        					list.add(val, tmp);
			    	        					val++;
			    	        				}
			    	        			}
			    	        		}
			    	        		
			    	        		if(val > 0)
			    	        		{
			    	        			key = row.getCell(0).toString();
			    	        			String[] values = list.toArray(new String[list.size()]);
			    	        			
			    	        			if(((String) registry.getDataTypeOfSheet().get(sheet_name)).equalsIgnoreCase("single"))
					    	        		dataMap.put(key, values[0]);
					    	        	else
					    	        		dataMap.put(key, values);
			    	        		}
			    	        	}
			    	        }
			    	   }
				 }
		    	  
			 }
			 
		 }catch(Exception e)
		 {
			 System.out.println("Error in reading data from excel sheet "+sheet_name+""+": "+e.getMessage());
		 }
		 
		 return dataMap;		 
	 }
	
	
	/**
	 * 
	 * @param sheet_name
	 * @return Data obtained which would be used as Data Provider
	 */
	public String[][] getDataForDataProvider(String sheet_name)
	{
		int endRow = 0;
		int endColumn=0;
		String [][] data = null;
		XSSFWorkbook workBook = null;
		try
		{
			int cx=0,cy=0;
			InputStream fs = new FileInputStream(registry.getRegisteredSheets().get(sheet_name));
			workBook = new XSSFWorkbook(fs);
			fs.close();
			
			if(workBook != null)
			   {
				     XSSFSheet sheet = workBook.getSheet(sheet_name);     
				     int itrIndex=0;
				     if(sheet != null)
				     {
				    	 endRow = sheet.getLastRowNum();
				       	 endColumn=sheet.getRow(0).getLastCellNum();
				    	 data = new String[endRow][endColumn];
					      for (int i = 1; i <= endRow; i++,cx++) 
					      {
					    	   cy=0;
						       XSSFRow list =  sheet.getRow(i);
						       Iterator<Cell> itr = list.iterator();
						       while(itr.hasNext())
						       {
						    	   data[cx][cy]=itr.next().toString();
						    	   cy++;
						    	   itrIndex++;
						       }
					      }
				     } 
			   }
			
		}catch(Exception e)
		{
			System.out.println("Error in reading data from excel sheet "+sheet_name+""+": "+e.getMessage());
		}
		return data;
	}
	 
}
