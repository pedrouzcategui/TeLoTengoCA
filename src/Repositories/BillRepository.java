/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repositories;

import Interfaces.Repository;
import Interfaces.RowConverter;
import Interfaces.RowMapper;
import Models.Bill;
import Models.BillDetail;
import Models.Product;
import Utils.CSVReader;
import Utils.Cypher;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author pedro
 */
public class BillRepository implements Repository<Bill> {

    private static final String FILE_NAME = "FACTURAS.CSV";

    // Esta funcion toma cada linea del csv y la convierte en un objeto de factura, eso es lo que hace un mapper.
    private static final RowMapper<Bill> billMapper = (String[] data) -> {
        if (data.length == 3) {
            int id = Integer.parseInt(data[0].trim());
            int salesmanId = Integer.parseInt(data[1].trim());
            Date date = new Date();
            return new Bill(id, salesmanId, date);
        }
        return null;
    };

    // Esta funcion toma una factura y lo convierte en una linea para CSV;
    private static final RowConverter<Bill> billConverter = (Bill b)
            -> b.getId() + ";" + b.getSalesmanId() + ";" + b.getBillDateAsString();

    private static final CSVReader<Bill> csvReader = new CSVReader<>(FILE_NAME, billMapper, ";");

    @Override
    public List<Bill> getAll() {
        return csvReader.getAll();
    }
    
    public int getSize(){
        return this.getAll().size();
    }
    
    // GetBillsByEmployeeId
    public List<Bill> getBillsByEmployeeId(int employeeId){
        return csvReader.getAll().stream()
                .filter(bill -> bill.getSalesmanId() == employeeId).toList();
    }

    @Override
    public Optional<Bill> findById(int id) {
        return csvReader.getAll().stream()
                .filter(bill -> bill.getId() == id)
                .findFirst();
    }
    
     /**
     * Añade una nueva factura al archivo CSV
     */
    public void addToCsv(Bill newBill){
        List<Bill> employees = getAll();
        employees.add(newBill);
        csvReader.writeToCSV(employees, billConverter);
    }
    
    public double getBillTotal(int billId) {
        double total = 0.0;

        BillDetailRepository billDetailRepository = new BillDetailRepository();
        ProductRepository productRepository = new ProductRepository();
        // 1. Get all BillDetail records for the given Bill ID
        List<BillDetail> billDetails = billDetailRepository.getBillDetailsByBillId(billId);

        // 2. Sum up each line: price * quantity (or adapt to your actual fields)
        for (BillDetail detail : billDetails) {
            Optional<Product> product = productRepository.findById(detail.getItemId());
            double linePrice = product.get().getPrice();
            int quantity = detail.getQuantitySold();
            total += (linePrice * quantity);
        }

        return total;
    }

    public void addToBillsFolder(Bill newBill) {
    // Repositories used to fetch BillDetail and Product information
    BillDetailRepository billDetailRepo = new BillDetailRepository();
    ProductRepository productRepo = new ProductRepository();
    EmployeeRepository employeeRepo = new EmployeeRepository();
    
    // 1. Retrieve all BillDetail records associated with this Bill
    List<BillDetail> billDetails = billDetailRepo.getBillDetailsByBillId(newBill.getId());
    
    // 2. Build the text content for the bill file
    StringBuilder sb = new StringBuilder();
    String employeeName = employeeRepo.findById(newBill.getSalesmanId()).get().getName();
    String employeeNameFormatted = String.join("-",employeeName.toLowerCase().split(" "));
    
    // Write the heading with date and salesman
    sb.append("Fecha: ").append(newBill.getBillDateAsString()).append("\n");
    sb.append("Vendedor: ").append(employeeName).append("\n\n");
    sb.append("------------------------DESGLOSE-------------------------------\n");
    
    sb.append("ID Item").append("     ")
              .append("Nombre").append("      ")
              .append("Cantidad").append("       ")
              .append("Precio").append("      ")
              .append("SubTotal").append("\n");
    
    double total = 0.0;
    
    // 3. For each BillDetail, retrieve Product, calculate subtotals, etc.
    for (BillDetail detail : billDetails) {
        Optional<Product> productOpt = productRepo.findById(detail.getItemId());
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            double lineSubtotal = product.getPrice() * detail.getQuantitySold();
            total += lineSubtotal;
            
            // Example line format:
            // itemId    itemName    quantity    price    $subtotal
            sb.append(detail.getItemId()).append("     ")
              .append(product.getName()).append("      ")
              .append(detail.getQuantitySold()).append("          ")
              .append(product.getPrice()).append("     $")
              .append(lineSubtotal).append("\n");
        }
    }
    
    sb.append("-------------------------------------------------------------------\n");
    sb.append("Total de factura:  $").append(total).append("\n");
    
    // 4. Create the "data/facturas/" folder if it does not exist and write the file
    long unixTime = System.currentTimeMillis() / 1000L; // seconds-based timestamp
    File folder = new File("data/facturas");
    if (!folder.exists()) {
        folder.mkdirs();
    }
    
    // 5. Build the file name using unix timestamp and salesmanName
    //    e.g. "1696893712-Pedro_Uzcategui.txt"
    String filename = unixTime + "-" + employeeNameFormatted;
    File outputFile = new File(folder, Cypher.encrypt(filename) + ".txt");
    
    try (PrintWriter writer = new PrintWriter(outputFile)) {
        writer.println(sb.toString());
        writer.flush();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
}
