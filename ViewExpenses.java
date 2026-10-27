/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sachith Nandakumar
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewExpenses {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        String user = "root";
        String password = "root";

        try {
            Connection con = DriverManager.getConnection(
                url, user, password
            );

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(
                "SELECT * FROM expenses"
            );

            System.out.println("ID | Name | Amount | Category | Date");
            System.out.println("------------------------------------");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("expense_name") + " | " +
                    rs.getBigDecimal("amount") + " | " +
                    rs.getString("category") + " | " +
                    rs.getDate("expense_date")
                );
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
