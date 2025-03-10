/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repositories;

import Interfaces.Repository;
import Interfaces.RowConverter;
import Interfaces.RowMapper;
import Models.Product;
import Utils.CSVReader;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements Repository<Product>  {
    private static final String FILE_NAME = "PRODUCTOS.CSV";
    private static final RowMapper<Product> productMapper = (String[] data) -> {
        if(data.length == 5){
            int id = Integer.parseInt(data[0].trim());
            String name = data[1].trim();
            float price = Float.parseFloat(data[2].trim());
            int quantity = Integer.parseInt(data[3].trim());
            int quantity_alarm = Integer.parseInt(data[4].trim());
            return new Product(id, name, price, quantity, quantity_alarm);
        }
        return null;
    };

    private static final RowConverter<Product> productConverter = (Product p) ->
        p.getId() + ";" + p.getName() + ";" + p.getPrice() + ";" + p.getQuantity() + ";" + p.getQuantityAlarm();

    private static final CSVReader<Product> csvReader = new CSVReader<>(FILE_NAME, productMapper, ";");

    @Override
    public List<Product> getAll() {
        return csvReader.getAll();
    }

    @Override
    public Optional<Product> findById(int id) {
        return csvReader.getAll().stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    /**
     * Metodo para actualizar productos
     */
    public boolean update(Product updatedProduct){
        List<Product> products = getAll(); // 1️⃣ Read all products from CSV

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) { // 2️⃣ Find the employee by ID
                products.set(i, updatedProduct); // 3️⃣ Replace the old entry with the new data
                csvReader.writeToCSV(products, productConverter); // 4️⃣ Write back everything to CSV
                return true; // ✅ Update was successful
            }
        }
        return false; // ❌ Employee not found
    }

}
