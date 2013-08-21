package com.framework.updatedb;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class setupDB {
	
    /**
     * @throws SAXException
     */
    public void setUpTestLinkDataBase() throws SQLException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException, IllegalAccessException, InstantiationException 
    {
        Connection conn = null;
        Connection iConn = null;
        Statement stmt = null;
        Statement iStmt = null;
        ResultSet rs = null;

        String insertStmt;
       // int result;

        ArrayList<String> tcName = new ArrayList<String>();

        try 
        {
             conn = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/testlink?"
                            + "user=root");

            iConn = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/testlink?"
                            + "user=root");

            stmt = conn.createStatement();
            iStmt = iConn.createStatement();

            rs = stmt.executeQuery("SELECT tc.id, tc.summary, nh.name, tc.importance FROM tcversions tc, nodes_hierarchy nh where tc.execution_type = 2 and tc.id-1 = nh.id");

            iStmt.executeUpdate("TRUNCATE TABLE automated_tests");

            while (rs.next()) {
                insertStmt = "INSERT INTO automated_tests (test_id, testcase_name, priority) VALUES ("
                        + rs.getString("id")
                        + ",'"
                        + rs.getString("name")
                        + "'," + rs.getString("importance") + ")";
                System.out.println(insertStmt);

                iStmt.executeUpdate(insertStmt);
            }

            rs = iStmt.executeQuery("SELECT test_id, testcase_name from automated_tests");

            while (rs.next()){
                tcName.add(rs.getString("test_id") + "#" + rs.getString("testcase_name"));
            }

            xmlParse xP = new xmlParse();
            String buildName = xP.getBuildName("test-output//testng-results.xml");

            System.out.println("******" + buildName);

            iStmt.executeUpdate("INSERT INTO builds (name, active, is_open) VALUES ('" + buildName + "', 1, 1)");

            System.out.println("Build Inserted*****");

            rs = iStmt.executeQuery("SELECT id as build_id FROM builds where name in (\"" + buildName + "\")");

            rs.next();

            String build_no;
            
            if(rs.getString("build_id").equalsIgnoreCase("0"))
                build_no = "1";
            else
                build_no = rs.getString("build_id");


            HashMap<String,String> tcDetails = new HashMap<String,String>();
            tcDetails = xP.parseXml("test-output//testng-results.xml");

            prepareResult pR = new prepareResult();
            pR.updateResult(tcDetails, tcName, buildName, build_no);

            System.out.println("Process Completed");

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        conn.close();
        iConn.close();
    }
}