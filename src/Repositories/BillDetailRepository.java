/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repositories;

import Interfaces.Repository;
import Interfaces.RowConverter;
import Interfaces.RowMapper;
import Models.BillDetail;
import Utils.CSVReader;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author pedro
 */
public class BillDetailRepository implements Repository<BillDetail> {

    private static final String FILE_NAME = "DETALLE_FACTURAS.CSV";

    // Esta funcion toma cada linea del csv y la convierte en un objeto de factura, eso es lo que hace un mapper.
    private static final RowMapper<BillDetail> billDetailMapper = (String[] data) -> {
        if (data.length == 4) {
            int id = Integer.parseInt(data[0].trim());
            int billId = Integer.parseInt(data[1].trim());
            int itemId = Integer.parseInt(data[2].trim());
            int itemQuantity = Integer.parseInt(data[3].trim());
            return new BillDetail(id, billId, itemId, itemQuantity);
        }
        return null;
    };

    // Esta funcion toma una factura y lo convierte en una linea para CSV;
    private static final RowConverter<BillDetail> billDetailConverter = (BillDetail b)
            -> b.getId() + ";" + b.getBillId()+ ";" + b.getItemId() + ";" + b.getQuantitySold();

    private static final CSVReader<BillDetail> csvReader = new CSVReader<>(FILE_NAME, billDetailMapper, ";");

    // Aqui es necesario el @override por la implementacion de la interfaz
    @Override
    public List<BillDetail> getAll() {
        return csvReader.getAll();
    }
    
    // Esto devuelve la longitud de filas en el archivo CSV, lo uso para determinar el ID de la siguiente factura
    public int getSize(){
        return this.getAll().size();
    }
    
    // Obtiene los objetos DetalleFactura (BillDetails) asociados con la factura (Bill)
    public List<BillDetail> getBillDetailsByBillId(int billId){
        return csvReader.getAll().stream()
                .filter(billDetail -> billDetail.getBillId() == billId).toList();
    }
    
    // Obtiene un DetalleFactura por su ID.
    @Override
    public Optional<BillDetail> findById(int id) {
        return csvReader.getAll().stream()
                .filter(billDetail -> billDetail.getId() == id)
                .findFirst();
    }
    
     /**
     * Añade una nueva factura al archivo CSV
     */
    public void addToCsv(BillDetail newBillDetail){
        List<BillDetail> billDetails = getAll();
        billDetails.add(newBillDetail);
        csvReader.writeToCSV(billDetails, billDetailConverter);
    }
    
    
    
}
