package com.bigcompany.service;

import com.bigcompany.model.Employee;
import com.bigcompany.model.ReportingLineAnomaly;
import com.bigcompany.model.SalaryAnomaly;
import com.bigcompany.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrganizationAnalyzerTest {

    private EmployeeRepository repository;
    private OrganizationAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        repository = new EmployeeRepository();
        analyzer = new OrganizationAnalyzer();
    }

    @Test
    void analyzeManagerSalaries_ManagerUnderpaid_ReturnsAnomaly() {
        Employee ceo = new Employee("1", "CEO", "A", 100000, null);
        Employee emp = new Employee("2", "Emp", "B", 100000, "1"); // Avg is 100k, min expected is 120k

        repository.addEmployee(ceo);
        repository.addEmployee(emp);

        List<SalaryAnomaly> anomalies = analyzer.analyzeManagerSalaries(repository);

        assertEquals(1, anomalies.size());
        assertEquals("1", anomalies.get(0).getManager().getId());
        assertEquals(SalaryAnomaly.Type.UNDERPAID, anomalies.get(0).getType());
        assertEquals(20000, anomalies.get(0).getDiscrepancy(), 0.01);
    }

    @Test
    void analyzeManagerSalaries_ManagerOverpaid_ReturnsAnomaly() {
        Employee ceo = new Employee("1", "CEO", "A", 200000, null);
        Employee emp1 = new Employee("2", "Emp1", "B", 100000, "1");
        Employee emp2 = new Employee("3", "Emp2", "B", 100000, "1"); // Avg is 100k, max expected is 150k

        repository.addEmployee(ceo);
        repository.addEmployee(emp1);
        repository.addEmployee(emp2);

        List<SalaryAnomaly> anomalies = analyzer.analyzeManagerSalaries(repository);

        assertEquals(1, anomalies.size());
        assertEquals("1", anomalies.get(0).getManager().getId());
        assertEquals(SalaryAnomaly.Type.OVERPAID, anomalies.get(0).getType());
        assertEquals(50000, anomalies.get(0).getDiscrepancy(), 0.01);
    }

    @Test
    void analyzeManagerSalaries_ManagerSalaryCorrect_ReturnsNoAnomaly() {
        Employee ceo = new Employee("1", "CEO", "A", 130000, null);
        Employee emp = new Employee("2", "Emp", "B", 100000, "1"); // Avg 100k, valid range 120k - 150k

        repository.addEmployee(ceo);
        repository.addEmployee(emp);

        List<SalaryAnomaly> anomalies = analyzer.analyzeManagerSalaries(repository);

        assertTrue(anomalies.isEmpty());
    }

    @Test
    void analyzeReportingLines_LineTooLong_ReturnsAnomaly() {
        // CEO
        repository.addEmployee(new Employee("1", "CEO", "A", 100000, null));
        // Manager 1 (1 mgr between CEO and Employee)
        repository.addEmployee(new Employee("2", "M1", "B", 80000, "1"));
        // Manager 2
        repository.addEmployee(new Employee("3", "M2", "C", 70000, "2"));
        // Manager 3
        repository.addEmployee(new Employee("4", "M3", "D", 60000, "3"));
        // Manager 4
        repository.addEmployee(new Employee("5", "M4", "E", 50000, "4"));
        // Employee (Has M1, M2, M3, M4 -> 4 managers -> expected: 4, actual: 4 -> valid)
        repository.addEmployee(new Employee("6", "E1", "F", 40000, "5"));
        // Employee too deep (Has M1, M2, M3, M4, E1(mgr) -> 5 managers -> invalid)
        repository.addEmployee(new Employee("7", "E2", "G", 30000, "6"));

        List<ReportingLineAnomaly> anomalies = analyzer.analyzeReportingLines(repository);

        assertEquals(1, anomalies.size());
        ReportingLineAnomaly anomaly = anomalies.get(0);
        assertEquals("7", anomaly.getEmployee().getId());
        assertEquals(5, anomaly.getActualManagersBetween());
        assertEquals(1, anomaly.getDiscrepancy());
    }
}
