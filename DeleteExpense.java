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

public class DeleteExpense {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        String user = "root";
        String password = "root";

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter expense ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM expenses WHERE id = ?";

        try {
            Connection con = DriverManager.getConnection(
                url, user, password
            );

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Expense deleted successfully!");
            } else {
                System.out.println("Expense ID not found.");
            }

            ps.close();
            con.close();
            sc.close();

        } catch (Exception e) {
            System.out.println("Failed to delete expense.");
            e.printStackTrace();
        }
    }
}
