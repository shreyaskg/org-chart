package com.bigcompany.service;

import com.bigcompany.model.Employee;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserServiceTest {

    @Test
    void parseCsv_ValidFile_ReturnsEmployees() throws IOException {
        String csvContent = "Id,firstName,lastName,salary,managerId\n" +
                "123,Joe,Doe,60000,\n" +
                "124,Martin,Chekov,45000,123\n" +
                "125,Bob,Ronstad,47000,123";

        Path tempFile = Files.createTempFile("employees", ".csv");
        Files.writeString(tempFile, csvContent);

        CsvParserService parser = new CsvParserService();
        List<Employee> employees = parser.parseCsv(tempFile.toString());

        assertEquals(3, employees.size());

        Employee ceo = employees.get(0);
        assertEquals("123", ceo.getId());
        assertTrue(ceo.getManagerId().isEmpty());

        Employee martin = employees.get(1);
        assertEquals("124", martin.getId());
        assertEquals(45000, martin.getSalary());
        assertEquals("123", martin.getManagerId().get());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void parseCsv_EmptyValues_HandledCorrectly() throws IOException {
        String csvContent = "Id,firstName,lastName,salary,managerId\n" +
                "123,Joe,Doe,60000,";

        Path tempFile = Files.createTempFile("employees2", ".csv");
        Files.writeString(tempFile, csvContent);

        CsvParserService parser = new CsvParserService();
        List<Employee> employees = parser.parseCsv(tempFile.toString());

        assertEquals(1, employees.size());
        assertTrue(employees.get(0).getManagerId().isEmpty());

        Files.deleteIfExists(tempFile);
    }
}
