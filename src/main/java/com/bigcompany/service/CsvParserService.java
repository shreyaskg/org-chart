package com.bigcompany.service;

import com.bigcompany.model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParserService {
    
    public List<Employee> parseCsv(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }

        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1) // Skip the header line
                    .filter(line -> line != null && !line.trim().isEmpty())
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        }
    }

    private Employee parseLine(String line) {
        // Split by comma
        String[] parts = line.split(",", -1);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid CSV line (less than 4 columns): " + line);
        }

        String id = parts[0].trim();
        String firstName = parts[1].trim();
        String lastName = parts[2].trim();
        double salary;
        try {
            salary = Double.parseDouble(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid salary format: " + parts[3].trim());
        }
        
        String managerId = parts.length > 4 ? parts[4].trim() : null;
        if (managerId != null && managerId.isEmpty()) {
            managerId = null;
        }

        return new Employee(id, firstName, lastName, salary, managerId);
    }
}
