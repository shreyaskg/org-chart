package com.bigcompany.model;

public class SalaryAnomaly {
    public enum Type {
        UNDERPAID,
        OVERPAID
    }

    private final Employee manager;
    private final Type type;
    private final double discrepancy;
    private final double expectedSalary;

    public SalaryAnomaly(Employee manager, Type type, double discrepancy, double expectedSalary) {
        this.manager = manager;
        this.type = type;
        this.discrepancy = discrepancy;
        this.expectedSalary = expectedSalary;
    }

    public Employee getManager() {
        return manager;
    }

    public Type getType() {
        return type;
    }

    public double getDiscrepancy() {
        return discrepancy;
    }

    public double getExpectedSalary() {
        return expectedSalary;
    }
}
