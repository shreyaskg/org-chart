package com.bigcompany.service;

import com.bigcompany.model.Employee;
import com.bigcompany.model.ReportingLineAnomaly;
import com.bigcompany.model.SalaryAnomaly;
import com.bigcompany.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

public class OrganizationAnalyzer {

    private static final double MIN_SALARY_FACTOR = 1.20; // 20% more
    private static final double MAX_SALARY_FACTOR = 1.50; // 50% more
    private static final int MAX_MANAGERS_BETWEEN = 4;

    public List<SalaryAnomaly> analyzeManagerSalaries(EmployeeRepository repository) {
        List<SalaryAnomaly> anomalies = new ArrayList<>();

        for (Employee employee : repository.getAllEmployees()) {
            List<Employee> subordinates = repository.getSubordinates(employee.getId());
            
            // Only analyze employees who are managers (have subordinates)
            if (subordinates.isEmpty()) {
                continue;
            }

            double averageSubordinateSalary = subordinates.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0.0);

            double minExpectedSalary = averageSubordinateSalary * MIN_SALARY_FACTOR;
            double maxExpectedSalary = averageSubordinateSalary * MAX_SALARY_FACTOR;

            if (employee.getSalary() < minExpectedSalary) {
                double discrepancy = minExpectedSalary - employee.getSalary();
                anomalies.add(new SalaryAnomaly(employee, SalaryAnomaly.Type.UNDERPAID, discrepancy, minExpectedSalary));
            } else if (employee.getSalary() > maxExpectedSalary) {
                double discrepancy = employee.getSalary() - maxExpectedSalary;
                anomalies.add(new SalaryAnomaly(employee, SalaryAnomaly.Type.OVERPAID, discrepancy, maxExpectedSalary));
            }
        }

        return anomalies;
    }

    public List<ReportingLineAnomaly> analyzeReportingLines(EmployeeRepository repository) {
        List<ReportingLineAnomaly> anomalies = new ArrayList<>();

        for (Employee employee : repository.getAllEmployees()) {
            int depth = calculateHierarchyDepth(employee, repository);
            
            // depth is the number of managers between employee and CEO.
            if (depth > MAX_MANAGERS_BETWEEN) {
                anomalies.add(new ReportingLineAnomaly(employee, depth, MAX_MANAGERS_BETWEEN));
            }
        }

        return anomalies;
    }

    private int calculateHierarchyDepth(Employee employee, EmployeeRepository repository) {
        int depth = 0;
        
        // Follow the chain of managers
        String currentManagerId = employee.getManagerId().orElse(null);
        while (currentManagerId != null) {
            Employee manager = repository.getEmployee(currentManagerId).orElse(null);
            if (manager == null) {
                break; // Manager not found in repository (broken data)
            }
            currentManagerId = manager.getManagerId().orElse(null);
            
            // Only count if the manager has a manager above them (i.e. they are not the CEO).
            // Actually, wait: the requirement says "more than 4 managers between them and the CEO".
            // Direct report to CEO -> 0 managers between.
            // Reports to X, who reports to CEO -> 1 manager between.
            // So if `currentManagerId` is NOT null, it means we found a manager who is NOT the CEO yet.
            if (currentManagerId != null) {
                depth++;
            }
        }
        return depth;
    }
}
