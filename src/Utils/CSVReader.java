/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import Interfaces.RowConverter;
import Interfaces.RowMapper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader<T> {
    private final String PATH_TO_CSV = "data/";
    private String filename = null;
    private String separator = ";";
    private RowMapper<T> rowMapper;

    public CSVReader(String filename, RowMapper<T> rowMapper, String separator) {
        this.setFilename(filename);
        this.setRowMapper(rowMapper);
        this.setSeparator(separator);
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getFilename() {
        return filename;
    }

    public void setRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }



    public String getFullFileNamePath(){
        return PATH_TO_CSV + this.filename;
    }

    public List<T> getAll(){
        List<T> records = new ArrayList<>();

        // Esta tecnica se llama try-with-resources, y cierra el archivo automaticamente por ti
        try (BufferedReader reader = new BufferedReader(new FileReader(this.getFullFileNamePath()))) {
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(this.getSeparator());
                // Solo para hacer debug:
                //System.out.println("Line Read: " + line);
                //System.out.println("Split Data Length: " + data.length);
                //System.out.println("Split Data: " + Arrays.toString(data));

                T obj = rowMapper.mapRow(data);
                if(obj != null){
                    records.add(obj);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    public void writeToCSV(List<T> records, RowConverter<T> rowConverter){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFullFileNamePath()))) {
            for (T record : records) {
                writer.write(rowConverter.convertToCSV(record));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo en el CSV: " + getFullFileNamePath(), e);
        }
    }

}
