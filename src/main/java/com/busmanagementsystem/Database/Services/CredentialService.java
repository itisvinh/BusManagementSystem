package com.busmanagementsystem.Database.Services;

import com.busmanagementsystem.Database.Configs.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class CredentialService {
    public static String authenticate(String username, String password, StringBuilder employeeID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Connection conn = DBConnection.getConn();
            preparedStatement = conn.prepareStatement("select EmployeeID from Credentials where username = ? and password = ?");

            String usr = SHA256.createHashString(username);
            String passwd = SHA256.createHashString(password);

            preparedStatement.setString(1, usr);
            preparedStatement.setString(2, passwd);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                String role = resultSet.getString("EmployeeID");

                if (employeeID != null)
                    employeeID.append(role);

                if (role.substring(0, 3).equals("ADM"))
                    return "admin";
                else
                    return "seller";
            }
            else
                return "non-exist";

        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        } finally {
            try { preparedStatement.close(); } catch (Exception e1) {}
            try { resultSet.close(); } catch (Exception e2) {}
        }
    }

}
