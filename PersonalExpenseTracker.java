/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sachith Nandakumar
 */

import java.sql.*;
import java.util.Scanner;

public class PersonalExpenseTracker {

    static final String URL =
        "jdbc:mysql://localhost:3306/expense_tracker";
    static final String USER = "root";
    static final String PASSWORD = "root";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== PERSONAL EXPENSE TRACKER ===");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number from 1 to 5.");
                continue;
            }

            switch (choice) {
                case 1: addExpense(); break;
                case 2: viewExpenses(); break;
                case 3: updateExpense(); break;
                case 4: deleteExpense(); break;
                case 5:
                    System.out.println("Thank you for using the tracker!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void addExpense() {
        try {
            System.out.print("Expense name: ");
            String name = sc.nextLine();

            System.out.print("Amount: ");
            double amount = Double.parseDouble(sc.nextLine());
            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            System.out.print("Category: ");
            String category = sc.nextLine();

            System.out.print("Date (YYYY-MM-DD): ");
            Date date = Date.valueOf(sc.nextLine());

            String sql = "INSERT INTO expenses "
                + "(expense_name, amount, category, expense_date) "
                + "VALUES (?, ?, ?, ?)";

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setDouble(2, amount);
                ps.setString(3, category);
                ps.setDate(4, date);
                ps.executeUpdate();
                System.out.println("Expense added successfully!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid amount or date. Use YYYY-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void viewExpenses() {
        String sql = "SELECT * FROM expenses ORDER BY expense_date DESC";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID | Name | Amount | Category | Date");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | "
                    + rs.getString("expense_name") + " | "
                    + rs.getBigDecimal("amount") + " | "
                    + rs.getString("category") + " | "
                    + rs.getDate("expense_date")
                );
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void updateExpense() {
        try {
            System.out.print("Expense ID to update: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("New name: ");
            String name = sc.nextLine();

            System.out.print("New amount: ");
            double amount = Double.parseDouble(sc.nextLine());
            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            System.out.print("New category: ");
            String category = sc.nextLine();

            System.out.print("New date (YYYY-MM-DD): ");
            Date date = Date.valueOf(sc.nextLine());

            String sql = "UPDATE expenses SET expense_name=?, amount=?, "
                + "category=?, expense_date=? WHERE id=?";

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setDouble(2, amount);
                ps.setString(3, category);
                ps.setDate(4, date);
                ps.setInt(5, id);

                if (ps.executeUpdate() > 0)
                    System.out.println("Expense updated successfully!");
                else
                    System.out.println("Expense ID not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Check the amount and date.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    static void deleteExpense() {
        try {
            System.out.print("Expense ID to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM expenses WHERE id=?";

            try (Connection con = connect();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);

                if (ps.executeUpdate() > 0)
                    System.out.println("Expense deleted successfully!");
                else
                    System.out.println("Expense ID not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid numeric ID.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
