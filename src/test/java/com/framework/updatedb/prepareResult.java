package com.framework.updatedb;

/**
 * Created with IntelliJ IDEA.
 * User: Dushyant Bhalgami
 * Date: 3/7/12
 * Time: 4:34 PM
 * Description: This class with receive the BuildName, TestCase Names & TestCase Execution details and insert this details into the automated_execution table.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class prepareResult {

    public void updateResult(HashMap<String,String> tcDetails, ArrayList<String> tcNames, String buildName, String buildNo) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Connection conn = null;
        Statement stmt = null;

       // int rs;

        String tcResult;

        String result = "n";
        String exe_ts;
        String error_message = "-";
        String stacktrace = "-";

        String insertStmt;

        conn = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/testlink?"
                        + "user=root");

        stmt = conn.createStatement();

        String testId;
        String testName;

        for (int i = 0; i < tcNames.size(); i++) {

            testId = tcNames.get(i).toString().split("#")[0];
            testName = tcNames.get(i).toString().split("#")[1];

            if(tcDetails.containsKey(testName))
                tcResult = tcDetails.get(testName).toString();
            else
                tcResult = "n";

            if (tcResult == "n") {
                insertStmt = "INSERT INTO automated_execution (id, build_id, build_name, test_name, result, error_message, stacktrace) VALUES (" + testId + "," + buildNo + ",'" + buildName + "','" + testName + "','n' , '-', '-')";

                 stmt.executeUpdate(insertStmt);
            } else {
                String[] details = tcResult.split("#");
                
                stacktrace = "-";
                error_message = "-";
                
                if (details[0].equalsIgnoreCase("PASS"))
                    result = "p";

                exe_ts = details[1];

                if (details[0].equalsIgnoreCase("FAIL")) {
                    result = "f";
                    error_message = details[2];
                    stacktrace = details[3];
                }
                
                if (details[0].equalsIgnoreCase("SKIP")) {
                    result = "n";
                    error_message = "-";
                    stacktrace = "-";
                }

                insertStmt = "INSERT INTO automated_execution (id, build_id, build_name, test_name, result, error_message, stacktrace, exe_ts) VALUES (" + testId + "," + buildNo + ",'" + buildName + "','" + testName + "','" + result + "','" + error_message + "','" + stacktrace + "','" + exe_ts + "')";

                stmt.executeUpdate(insertStmt);
            }
        }
        conn.close();

    }
}
