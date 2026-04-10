package com.bigcompany;

import com.bigcompany.console.ReportPrinter;
import com.bigcompany.model.Employee;
import com.bigcompany.model.ReportingLineAnomaly;
import com.bigcompany.model.SalaryAnomaly;
import com.bigcompany.repository.EmployeeRepository;
import com.bigcompany.service.CsvParserService;
import com.bigcompany.service.OrganizationAnalyzer;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            logger.warn("No CSV file path provided as an argument. Defaulting to 'employees.csv'.");
            filePath = "employees.csv";
        } else {
            filePath = args[0];
        }

        try {
            // 1. Parsing
            CsvParserService parser = new CsvParserService();
            List<Employee> employees = parser.parseCsv(filePath);

            // 2. Repository Load
            EmployeeRepository repository = new EmployeeRepository();
            repository.addAll(employees);

            // 3. Analysis
            OrganizationAnalyzer analyzer = new OrganizationAnalyzer();
            List<SalaryAnomaly> salaryAnomalies = analyzer.analyzeManagerSalaries(repository);
            List<ReportingLineAnomaly> reportingLineAnomalies = analyzer.analyzeReportingLines(repository);

            // 4. Output
            ReportPrinter printer = new ReportPrinter();
            printer.printReport(salaryAnomalies, reportingLineAnomalies);

        } catch (IOException e) {
            logger.error("Error reading the file: {}", e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            logger.error("Error parsing the file content: {}", e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
