package Views;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Models.Bill;
import Models.BillDetail;
import Models.Log;
import Models.Product;
import Repositories.BillDetailRepository;
import Repositories.BillRepository;
import Repositories.LogRepository;
import Repositories.ProductRepository;
import State.AppContext;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.EventObject;
import java.util.Optional;

public class VendorScreen extends JFrame {

    private JTextField productIdField;
    private JButton addButton;
    private JButton generateBillButton;
    private JTable productTable;
    private JLabel totalLabel;
    private JLabel salesmanLabel;
    private DefaultTableModel tableModel;
    private double total = 0.00;

    // Column indices for easy reference
    private static final int COL_ID = 0;
    private static final int COL_NAME = 1;
    private static final int COL_PRICE = 2;
    private static final int COL_QUANTITY = 3;
    private static final int COL_SUBTOTAL = 4;
    private static final int COL_ACTION = 5;

    public VendorScreen() {
        setTitle("Vendor Screen");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        setLocationRelativeTo(null);

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout());

        
        
        productIdField = new JTextField(10);
        addButton = new JButton("Añadir Producto");

        JButton generateBillButton = new JButton("Generar Factura");

        topPanel.add(new JLabel("Vendedor: " + AppContext.getInstance().getCurrentEmployee().getName() + " - "));
        topPanel.add(new JLabel("Product ID:"));
        topPanel.add(productIdField);
        topPanel.add(addButton);
        topPanel.add(generateBillButton);
        add(topPanel, BorderLayout.NORTH);

        // Table Setup (custom DefaultTableModel)
        String[] columnNames = {"ID", "Product", "Price", "Quantity", "Subtotal", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow editing only on Quantity (COL_QUANTITY) and Action (COL_ACTION)
                // We will handle Action column with a custom editor (button)
                // but we must return true for the editor to be activated.
                return (column == COL_QUANTITY || column == COL_ACTION);
            }
        };
        productTable = new JTable(tableModel);
        productTable.setRowHeight(28);

        // Make Price and Subtotal columns appear as currency with alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        productTable.getColumnModel().getColumn(COL_PRICE).setCellRenderer(rightRenderer);
        productTable.getColumnModel().getColumn(COL_SUBTOTAL).setCellRenderer(rightRenderer);

        // Add custom cell editor for Quantity (spinner)
        productTable.getColumnModel().getColumn(COL_QUANTITY).setCellEditor(new SpinnerEditor());

        // Add custom renderer+editor for Delete button
        productTable.getColumnModel().getColumn(COL_ACTION).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(COL_ACTION).setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Bottom Panel for Total Price
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: $0.00");
        bottomPanel.add(totalLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listen for table changes (e.g., quantity changes) to recalc totals
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // If quantity changed, recalc that row's subtotal and total
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == COL_QUANTITY) {
                    int row = e.getFirstRow();
                    recalcSubtotal(row);
                    recalcTotal();
                }
            }
        });

        // Button Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        // Generar Factura Listener
        generateBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Generar Factura
                BillRepository billRepository = new BillRepository();
                int employeeId = AppContext.getInstance().getCurrentEmployee().getId();
                Bill newBill = new Bill(billRepository.getSize() + 1, employeeId, new Date());
                billRepository.addToCsv(newBill);

                // Crear N cantidad de BillDetail() dependiendo de la cantidad de productos
                // Cantidad de productos:
                BillDetailRepository billDetailRepository = new BillDetailRepository();
                int productCount = tableModel.getRowCount();
                // Actualizar el numero de cantidades disponibles para los productos seleccionados
                for (int i = 0; i < productCount; i++) {
                    int productId = (int) tableModel.getValueAt(i, COL_ID);
                    int productQuantity = (int) tableModel.getValueAt(i, COL_QUANTITY);
                    int billDetailId = billDetailRepository.getSize() + 1;
                    BillDetail newBillDetail = new BillDetail(billDetailId, newBill.getId(), productId, productQuantity);
                    billDetailRepository.addToCsv(newBillDetail);
                }
                billRepository.addToBillsFolder(newBill);

                LogRepository logRepo = new LogRepository();
                logRepo.addToCsv(new Log(new Date(), 
                        AppContext.getInstance().getCurrentEmployee().getId(), 
                        AppContext.getInstance().getCurrentEmployee().getName(),
                        "Nueva Factura con ID: " + newBill.getId() + " Generada"
                ));
                
                JOptionPane.showMessageDialog(null, "Nueva Factura con ID " + newBill.getId() + " ha sido generada", "Nueva Factura Generada", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
    }

    private void addProduct() {
        int productId = Integer.parseInt(productIdField.getText().trim());
        ProductRepository productRepo = new ProductRepository();
        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            // Default quantity 1, subtotal = price * quantity
            int quantity = 1;
            double subtotal = product.get().getPrice() * quantity;

            Object[] row = {
                product.get().getId(),
                product.get().getName(),
                product.get().getPrice(),
                quantity,
                subtotal,
                "Delete"
            };
            tableModel.addRow(row);

            productIdField.setText("");

            // Update total
            recalcTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Recalculate a single row's subtotal based on quantity * price
    private void recalcSubtotal(int row) {
        // Safely read price as Number
        Number priceNum = (Number) tableModel.getValueAt(row, COL_PRICE);
        double price = priceNum.doubleValue();

        // Safely read quantity as Number
        Number quantityNum = (Number) tableModel.getValueAt(row, COL_QUANTITY);
        double quantity = quantityNum.doubleValue();

        double newSubtotal = price * quantity;
        tableModel.setValueAt(newSubtotal, row, COL_SUBTOTAL);
    }

    // Recalculate total of all rows
    private void recalcTotal() {
        double newTotal = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double subtotal = (double) tableModel.getValueAt(i, COL_SUBTOTAL);
            newTotal += subtotal;
        }
        total = newTotal;
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VendorScreen().setVisible(true));
    }
}

// SpinnerEditor: allows a JSpinner in the Quantity column
class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

    private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // Set current value
        spinner.setValue(value);
        return spinner;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}

// ButtonRenderer: displays a "Delete" button in the Action column
class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        setText((value == null) ? "Delete" : value.toString());
        return this;
    }
}

// ButtonEditor: handles clicks on the Delete button
class ButtonEditor extends DefaultCellEditor {

    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        label = (value == null) ? "Delete" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Remove the row
            int row = table.getSelectedRow();
            if (row >= 0) {
                ((DefaultTableModel) table.getModel()).removeRow(row);
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
