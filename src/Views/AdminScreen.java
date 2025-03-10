package Views;

import Models.Bill;
import Models.Employee;
import Models.Log;
import Repositories.BillRepository;
import Repositories.BillDetailRepository;
import Repositories.EmployeeRepository;
import Repositories.LogRepository;
import State.AppContext;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminScreen extends JFrame {

    private BillRepository billRepository;
    private BillDetailRepository billDetailRepository;
    private JLabel welcomeLabel;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton searchButton;
    private JButton generateReportButton;
    private JTable salesTable;
    private DefaultTableModel tableModel;

    public AdminScreen() {
        // Initialize repositories
        billRepository = new BillRepository();
        billDetailRepository = new BillDetailRepository();

        // Initialize UI components
        initComponents();
        searchSales();
    }

    private void initComponents() {
        setTitle("Admin Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        // Welcome label
        Employee currentEmployee = AppContext.getInstance().getCurrentEmployee();
        welcomeLabel = new JLabel("Welcome, " + currentEmployee.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        // Date range input
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new FlowLayout());
        datePanel.add(new JLabel("Start Date:"));

        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date startDate = calendar.getTime();

        startDateSpinner = new JSpinner(new SpinnerDateModel(startDate, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startDateEditor);
        datePanel.add(startDateSpinner);

        datePanel.add(new JLabel("End Date:"));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        endDate = calendar.getTime();
        endDateSpinner = new JSpinner(new SpinnerDateModel(endDate, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endDateEditor);
        datePanel.add(endDateSpinner);

        searchButton = new JButton("Search");
        datePanel.add(searchButton);
        
        generateReportButton = new JButton("Generar Reporte");
        datePanel.add(generateReportButton);
        
        add(datePanel, BorderLayout.CENTER);

        // Sales table
        tableModel = new DefaultTableModel(new Object[]{"Vendedor ID", "Nombre Vendedor", "ID Factura", "Total Venta"}, 0);
        salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        add(scrollPane, BorderLayout.SOUTH);

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSales();
            }
        });
        
        generateReportButton.addActionListener(e -> generateReport());
    }

    private void searchSales() {
        try {
            EmployeeRepository employeeRepository = new EmployeeRepository();
            // 1) Retrieve the start/end dates from the JSpinner components
            Date startDate = (Date) startDateSpinner.getValue();
            Date endDate = (Date) endDateSpinner.getValue();

            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);

            // 2) Get all bills, then filter by date
            List<Bill> bills = billRepository.getAll().stream()
                    .filter(bill -> {
                        Date createdAt = bill.getCreatedAt(); // If your Bill model has getCreatedAt()
                        // Or adapt if you store the date differently
                        return (createdAt != null
                                && !createdAt.before(startDate)
                                && !createdAt.after(endDate));
                    })
                    .collect(Collectors.toList());

            System.out.println("Filtered Bills: " + bills);

            // 3) Clear existing rows in the table
            tableModel.setRowCount(0);

            // 4) For each Bill, gather the required data and add a row to the table
            for (Bill bill : bills) {
                int salesmanId = bill.getSalesmanId();
                int billId = bill.getId();

                // Suppose you have an EmployeeRepository to get the name
                // Replace with your actual lookup code if it's different
                Employee salesman = employeeRepository.findById(salesmanId).orElse(null);
                String salesmanName = (salesman != null) ? salesman.getName() : "Unknown";

                // Use the new getBillTotal() method from BillRepository
                double totalSale = billRepository.getBillTotal(billId);

                // Add the row to your table
                tableModel.addRow(new Object[]{
                    salesmanId,
                    salesmanName,
                    billId,
                    totalSale
                });
            }

            // 5) Debug: print table rows in the console
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                System.out.println("Row " + i + ": "
                        + tableModel.getValueAt(i, 0) + ", "
                        + tableModel.getValueAt(i, 1) + ", "
                        + tableModel.getValueAt(i, 2) + ", "
                        + tableModel.getValueAt(i, 3));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while processing the dates.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReport() {
        try {
            // A) Make sure the "data/reports" folder exists
            java.io.File reportDir = new java.io.File("data/reportes");
            if (!reportDir.exists()) {
                reportDir.mkdirs(); // create directories if not present
            }
    
            // B) Get start/end dates from spinners
            Date startDate = (Date) startDateSpinner.getValue();
            Date endDate = (Date) endDateSpinner.getValue();
    
            // C) Filter bills by date
            EmployeeRepository employeeRepository = new EmployeeRepository();
            List<Bill> bills = billRepository.getAll().stream()
                    .filter(bill -> {
                        Date createdAt = bill.getCreatedAt();
                        return (createdAt != null
                                && !createdAt.before(startDate)
                                && !createdAt.after(endDate));
                    })
                    .collect(Collectors.toList());
    
            // D) Create the file with a unique name {unix_timestamp}_report.txt
            String fileName = System.currentTimeMillis() + "_report.txt";
            java.io.File reportFile = new java.io.File(reportDir, fileName);
    
            // E) Write the sales data for each bill into the file
            try (java.io.PrintWriter writer = new java.io.PrintWriter(
                    new java.io.FileWriter(reportFile))) {
    
                // Optionally write a header line
                writer.println("Reporte de ventas");
                writer.println("Desde: " + startDate + " Hasta: " + endDate);
                writer.println("-----------------------------------------");
    
                for (Bill bill : bills) {
                    int salesmanId = bill.getSalesmanId();
                    int billId = bill.getId();
    
                    // Look up salesman name
                    Employee salesman = employeeRepository.findById(salesmanId).orElse(null);
                    String salesmanName = (salesman != null) ? salesman.getName() : "Desconocido";
    
                    // Calculate the total of this bill
                    double totalSale = billRepository.getBillTotal(billId);
    
                    // Format a line of text for the file
                    // e.g. "SalesmanID | Name | BillID | Total"
                    writer.printf("%d | %s | ID Factura: %d | Total: %.2f%n",
                                  salesmanId, salesmanName, billId, totalSale);
                }
            }
    
            // F) Optionally notify the user that the report was created
            JOptionPane.showMessageDialog(
                this,
                "Reporte Generado de manera satisfactoria:\n" + reportFile.getAbsolutePath(),
                "Reporte Generado",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Log here
            LogRepository logRepo = new LogRepository();
            logRepo.addToCsv(new Log(new Date(), 
                    AppContext.getInstance().getCurrentEmployee().getId(), 
                    AppContext.getInstance().getCurrentEmployee().getName(),
                    "Generación de un nuevo reporte: " + fileName
            ));
    
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "An error occurred while generating the report.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminScreen adminScreen = new AdminScreen();
            adminScreen.setVisible(true);
        });
    }
}
