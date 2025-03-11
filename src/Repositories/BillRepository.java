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

    // Funcion que obtiene todas las facturas del CSV como objetos de tipo Bill
    @Override
    public List<Bill> getAll() {
        return csvReader.getAll();
    }
    
    // Funcion para obtener el numero de filas del CSV
    public int getSize(){
        return this.getAll().size();
    }
    
    // Obtiene las facturas de un vendedor determinado
    public List<Bill> getBillsByEmployeeId(int employeeId){
        return csvReader.getAll().stream()
                .filter(bill -> bill.getSalesmanId() == employeeId).toList();
    }

    // Obtiene una factura por su ID
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
    
    // Funcion de tipo utility que se usa para obtener el total de una factura.
    public double getBillTotal(int billId) {
        
        double total = 0.0;

        // Se instancian los repositorios que se encargan de los metodos de guardado de datos
        BillDetailRepository billDetailRepository = new BillDetailRepository();
        ProductRepository productRepository = new ProductRepository();
        // Se obtiene la factura por su ID
        List<BillDetail> billDetails = billDetailRepository.getBillDetailsByBillId(billId);

        // Se suman los precios del producto y se multiplican por sus cantidades
        for (BillDetail detail : billDetails) {
            Optional<Product> product = productRepository.findById(detail.getItemId());
            double linePrice = product.get().getPrice();
            int quantity = detail.getQuantitySold();
            total += (linePrice * quantity);
        }

        return total;
    }

    public void addToBillsFolder(Bill newBill) {
    // Se instancian los repositorios necesarios para la creacion de la factura
    BillDetailRepository billDetailRepo = new BillDetailRepository();
    ProductRepository productRepo = new ProductRepository();
    EmployeeRepository employeeRepo = new EmployeeRepository();
    
    // Se obtienen todos los DetalleFactura asociados a la Factura.
    List<BillDetail> billDetails = billDetailRepo.getBillDetailsByBillId(newBill.getId());
    
    // Usamos la clase StringBuilder para poder crear el archivo
    StringBuilder sb = new StringBuilder();
    // Se obtiene el nombre del empleado
    String employeeName = employeeRepo.findById(newBill.getSalesmanId()).get().getName();
    // Se formatea el nombre del empleado para el archivo
    String employeeNameFormatted = String.join("-",employeeName.toLowerCase().split(" "));
    
    // Header de la factura
    sb.append("Fecha: ").append(newBill.getBillDateAsString()).append("\n");
    sb.append("Vendedor: ").append(employeeName).append("\n\n");
    sb.append("Razon Social: ").append("TeLoTengo C.A").append("\n\n");
    sb.append("------------------------DESGLOSE-------------------------------\n");
    
    sb.append("ID Item").append("     ")
              .append("Nombre").append("      ")
              .append("Cantidad").append("       ")
              .append("Precio").append("      ")
              .append("SubTotal").append("\n");
    
    double total = 0.0;
    
    // Calcula subtotales de cada DetalleFactura
    for (BillDetail detail : billDetails) {
        Optional<Product> productOpt = productRepo.findById(detail.getItemId());
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            double lineSubtotal = product.getPrice() * detail.getQuantitySold();
            total += lineSubtotal;
            
            // Formato de la linea
            // id    producto    cantidad     precio    subtotal
            sb.append(detail.getItemId()).append("     ")
              .append(product.getName()).append("      ")
              .append(detail.getQuantitySold()).append("          ")
              .append(product.getPrice()).append("     $")
              .append(lineSubtotal).append("\n");
        }
    }
    
    sb.append("-------------------------------------------------------------------\n");
    sb.append("Total de factura:                        $").append(total).append("\n");
    
    // Se toma el milisegundo de unix para asegurarnos que los archivos sean unicos
    long unixTime = System.currentTimeMillis() / 1000L;
    // Se crea o sobreescribe la carpeta facturas
    File folder = new File("data/facturas");
    // Si no existe el folder, se crea
    if (!folder.exists()) {
        folder.mkdirs();
    }
    
    // 5. Se crea el nombre del archivo, y luego se cirfa.
    // e.g. "1696893712-Pedro_Uzcategui.txt"
    String filename = unixTime + "-" + employeeNameFormatted;
    File outputFile = new File(folder, Cypher.encrypt(filename) + ".txt");
    
    // Se genera el archivo de salida y se cierra el writer.
    try (PrintWriter writer = new PrintWriter(outputFile)) {
        writer.println(sb.toString());
        writer.flush();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
}
