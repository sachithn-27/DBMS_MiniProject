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
import java.sql.PreparedStatement;
import java.util.Scanner;

public class AddExpense {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        String user = "root";
        String password = "root";

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter expense name: ");
        String name = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        String sql = "INSERT INTO expenses "
                   + "(expense_name, amount, category, expense_date) "
                   + "VALUES (?, ?, ?, ?)";

        try {
            Connection con = DriverManager.getConnection(
                url, user, password
            );

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setDouble(2, amount);
            ps.setString(3, category);
            ps.setDate(4, java.sql.Date.valueOf(date));

            ps.executeUpdate();

            System.out.println("Expense added successfully!");

            ps.close();
            con.close();
            sc.close();

        } catch (Exception e) {
            System.out.println("Failed to add expense.");
            e.printStackTrace();
        }
    }
}
