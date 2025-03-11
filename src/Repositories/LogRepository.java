package Repositories;

import Interfaces.Repository;
import Interfaces.RowConverter;
import Interfaces.RowMapper;
import Models.Log;
import Utils.CSVReader;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class LogRepository implements Repository<Log> {

    private static final String FILE_NAME = "LOGS.csv";

    private static final RowMapper<Log> logMapper = (String[] data) -> {
        if (data.length == 4) {
            Date createdAt = new Date();
            int userId = Integer.parseInt(data[1].trim());
            String username = data[2].trim();
            String action = data[3].trim();
            return new Log(createdAt, userId, username, action);
        }
        return null;
    };

    private static final RowConverter<Log> logConverter = (Log log)
            -> log.getCreatedAtAsString() + ";" + log.getUserId() + ";" + log.getUsername() + ";" + log.getAction();

    private final CSVReader<Log> csvReader;

    public LogRepository() {
        this.csvReader = new CSVReader<>(FILE_NAME, logMapper, ";");
    }

    @Override
    public List<Log> getAll() {
        return csvReader.getAll();
    }

    // Esta funcion no se necesita para los logs pero esta definida en las interfaces
    @Override
    public Optional<Log> findById(int id) {
        return Optional.empty();
    }

    public void addToCsv(Log newLog) {
        List<Log> logs = getAll();
        logs.add(newLog);
        csvReader.writeToCSV(logs, logConverter);
    }
}