/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sachith Nandakumar
 */



import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;

public class ExpenseTrackerGUI extends JFrame {

    private JTextField nameField, amountField, categoryField, dateField;
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel, countLabel;

    private final String URL =
        "jdbc:mysql://localhost:3306/expense_tracker";
    private final String USER = "root";
    private final String PASSWORD = "root";

    private final Color NAVY = new Color(25, 38, 65);
    private final Color BLUE = new Color(59, 110, 230);
    private final Color GREEN = new Color(22, 150, 105);
    private final Color RED = new Color(215, 65, 75);
    private final Color BG = new Color(244, 247, 252);
    private final Color TEXT = new Color(42, 52, 70);

    public ExpenseTrackerGUI() {
        setTitle("Personal Expense Tracker");
        setSize(1100, 720);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);
        setContentPane(root);

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(
            BorderFactory.createEmptyBorder(20, 28, 20, 28)
        );

        JLabel title = new JLabel("Personal Expense Tracker");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Manage your daily spending in one place"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(205, 216, 235));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(
            new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BG);
        content.setBorder(
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        );
        root.add(content, BorderLayout.CENTER);

        // TOTAL EXPENSE CARD
        JPanel summary = new JPanel(new BorderLayout(10, 5));
        summary.setBackground(Color.WHITE);
        summary.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(225, 231, 241)
            ),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel totalTitle = new JLabel("TOTAL EXPENSES");
        totalTitle.setFont(
            new Font("SansSerif", Font.BOLD, 12)
        );
        totalTitle.setForeground(new Color(105, 117, 137));

        totalLabel = new JLabel("0.00");
        totalLabel.setFont(
            new Font("SansSerif", Font.BOLD, 30)
        );
        totalLabel.setForeground(NAVY);

        countLabel = new JLabel("0 records");
        countLabel.setFont(
            new Font("SansSerif", Font.PLAIN, 13)
        );
        countLabel.setForeground(new Color(105, 117, 137));

        JPanel summaryText = new JPanel();
        summaryText.setOpaque(false);
        summaryText.setLayout(
            new BoxLayout(summaryText, BoxLayout.Y_AXIS)
        );
        summaryText.add(totalTitle);
        summaryText.add(Box.createVerticalStrut(6));
        summaryText.add(totalLabel);
        summaryText.add(Box.createVerticalStrut(3));
        summaryText.add(countLabel);

        summary.add(summaryText, BorderLayout.WEST);
        content.add(summary, BorderLayout.NORTH);

        // FORM AND TABLE AREA
        JPanel workspace = new JPanel(new BorderLayout(0, 16));
        workspace.setOpaque(false);
        content.add(workspace, BorderLayout.CENTER);

        // INPUT FORM
        JPanel formCard = new JPanel(new BorderLayout(0, 12));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(225, 231, 241)
            ),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel formTitle = new JLabel("Expense Details");
        formTitle.setFont(
            new Font("SansSerif", Font.BOLD, 17)
        );
        formTitle.setForeground(TEXT);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 4, 12, 8));
        fields.setOpaque(false);

        nameField = new JTextField();
        amountField = new JTextField();
        categoryField = new JTextField();
        dateField = new JTextField();

        fields.add(createField("Expense Name", nameField));
        fields.add(createField("Amount", amountField));
        fields.add(createField("Category", categoryField));
        fields.add(createField("Date (YYYY-MM-DD)", dateField));

        formCard.add(fields, BorderLayout.CENTER);

        // BUTTONS
        JButton addButton = createButton("Add Expense", GREEN);
        JButton viewButton = createButton("Refresh Table", BLUE);
        JButton updateButton = createButton("Update Selected", NAVY);
        JButton deleteButton = createButton("Delete Selected", RED);
        JButton clearButton = createButton("Clear Fields",
            new Color(100, 113, 132));

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 9, 0)
        );
        buttons.setOpaque(false);

        buttons.add(addButton);
        buttons.add(viewButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        formCard.add(buttons, BorderLayout.SOUTH);
        workspace.add(formCard, BorderLayout.NORTH);

        // EXPENSE TABLE
        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(225, 231, 241)
            ),
            BorderFactory.createEmptyBorder(14, 16, 16, 16)
        ));

        JLabel tableTitle = new JLabel("Your Expenses");
        tableTitle.setFont(
            new Font("SansSerif", Font.BOLD, 17)
        );
        tableTitle.setForeground(TEXT);
        tableCard.add(tableTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{
                "ID", "Expense Name", "Amount",
                "Category", "Date"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(233, 237, 244));
        table.setFillsViewportHeight(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(0, 38));
        tableHeader.setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );
        tableHeader.setBackground(
            new Color(232, 238, 248)
        );
        tableHeader.setForeground(NAVY);
        tableHeader.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class,
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean selected,
                    boolean focus, int row, int column) {

                    Component c = super.getTableCellRendererComponent(
                        t, value, selected, focus, row, column
                    );

                    if (selected) {
                        c.setBackground(new Color(214, 228, 255));
                        c.setForeground(NAVY);
                    } else {
                        c.setBackground(row % 2 == 0
                            ? Color.WHITE
                            : new Color(247, 249, 253));
                        c.setForeground(TEXT);
                    }

                    setBorder(BorderFactory.createEmptyBorder(
                        0, 10, 0, 10
                    ));

                    if (column == 0 || column == 2) {
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }

                    return c;
                }
            }
        );

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(
            new Color(230, 235, 243)
        ));
        tableCard.add(scroll, BorderLayout.CENTER);
        workspace.add(tableCard, BorderLayout.CENTER);

        // SELECT A ROW TO LOAD ITS DETAILS
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row >= 0) {
                    nameField.setText(
                        model.getValueAt(row, 1).toString()
                    );
                    amountField.setText(
                        model.getValueAt(row, 2).toString()
                    );
                    categoryField.setText(
                        model.getValueAt(row, 3).toString()
                    );
                    dateField.setText(
                        model.getValueAt(row, 4).toString()
                    );
                }
            }
        });

        // BUTTON ACTIONS
        addButton.addActionListener(e -> addExpense());
        viewButton.addActionListener(e -> viewExpenses());
        updateButton.addActionListener(e -> updateExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        clearButton.addActionListener(e -> clearFields());

        // LOAD SAVED DATA
        viewExpenses();
    }

    private JPanel createField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        JLabel caption = new JLabel(label);
        caption.setFont(
            new Font("SansSerif", Font.BOLD, 12)
        );
        caption.setForeground(new Color(82, 95, 116));

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(150, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(211, 220, 234)
            ),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        panel.add(caption, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(
            new Font("SansSerif", Font.BOLD, 12)
        );
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        button.setMargin(new Insets(10, 12, 10, 12));
        return button;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()
                || amountField.getText().trim().isEmpty()
                || categoryField.getText().trim().isEmpty()
                || dateField.getText().trim().isEmpty()) {
            showMessage("Please fill in all fields.");
            return false;
        }

        try {
            BigDecimal amount = new BigDecimal(
                amountField.getText().trim()
            );

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showMessage("Amount must be greater than zero.");
                return false;
            }

            if (amount.scale() > 2) {
                showMessage("Amount can have at most 2 decimal places.");
                return false;
            }

            if (amount.precision() - amount.scale() > 8) {
                showMessage("Amount is too large.");
                return false;
            }

            java.sql.Date.valueOf(dateField.getText().trim());

        } catch (NumberFormatException ex) {
            showMessage("Enter a valid amount, e.g. 125.50.");
            return false;
        } catch (IllegalArgumentException ex) {
            showMessage("Use date format YYYY-MM-DD.");
            return false;
        }

        return true;
    }

    private void clearFields() {
        nameField.setText("");
        amountField.setText("");
        categoryField.setText("");
        dateField.setText("");
        table.clearSelection();
    }

    private void addExpense() {
        if (!validateInput()) return;

        String sql = "INSERT INTO expenses "
            + "(expense_name, amount, category, expense_date) "
            + "VALUES (?, ?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nameField.getText().trim());
            ps.setBigDecimal(2,
                new BigDecimal(amountField.getText().trim()));
            ps.setString(3, categoryField.getText().trim());
            ps.setDate(4,
                java.sql.Date.valueOf(dateField.getText().trim()));

            ps.executeUpdate();

            showMessage("Expense added successfully!");
            clearFields();
            viewExpenses();

        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void viewExpenses() {
        model.setRowCount(0);

        String sql = "SELECT id, expense_name, amount, "
            + "category, expense_date FROM expenses ORDER BY id";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("expense_name"),
                    rs.getBigDecimal("amount"),
                    rs.getString("category"),
                    rs.getDate("expense_date")
                });
            }

            updateSummary();

        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void updateSummary() {
        String sql = "SELECT COALESCE(SUM(amount), 0), "
            + "COUNT(*) FROM expenses";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                int count = rs.getInt(2);

                totalLabel.setText(
                    "₹ " + total.toPlainString()
                );
                countLabel.setText(
                    count + (count == 1 ? " record" : " records")
                );
            }

        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void updateExpense() {
        int row = table.getSelectedRow();

        if (row < 0) {
            showMessage("Select an expense from the table first.");
            return;
        }

        if (!validateInput()) return;

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        String sql = "UPDATE expenses SET expense_name=?, "
            + "amount=?, category=?, expense_date=? WHERE id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nameField.getText().trim());
            ps.setBigDecimal(2,
                new BigDecimal(amountField.getText().trim()));
            ps.setString(3, categoryField.getText().trim());
            ps.setDate(4,
                java.sql.Date.valueOf(dateField.getText().trim()));
            ps.setInt(5, id);

            if (ps.executeUpdate() > 0) {
                showMessage("Expense updated successfully!");
                clearFields();
                viewExpenses();
            } else {
                showMessage("Expense not found.");
            }

        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void deleteExpense() {
        int row = table.getSelectedRow();

        if (row < 0) {
            showMessage("Select an expense to delete.");
            return;
        }

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Delete the selected expense?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM expenses WHERE id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                showMessage("Expense deleted successfully!");
                clearFields();
                viewExpenses();
            } else {
                showMessage("Expense not found.");
            }

        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
            this, message, "Personal Expense Tracker",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showDatabaseError(SQLException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Database error:\n" + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
                // Use the default Swing appearance if unavailable.
            }

            new ExpenseTrackerGUI().setVisible(true);
        });
    }
}
