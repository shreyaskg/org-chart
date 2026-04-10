package com.bigcompany.model;

public class ReportingLineAnomaly {
    private final Employee employee;
    private final int actualManagersBetween;
    private final int expectedManagersBetween;

    public ReportingLineAnomaly(Employee employee, int actualManagersBetween, int expectedManagersBetween) {
        this.employee = employee;
        this.actualManagersBetween = actualManagersBetween;
        this.expectedManagersBetween = expectedManagersBetween;
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getActualManagersBetween() {
        return actualManagersBetween;
    }
    
    public int getDiscrepancy() {
        return actualManagersBetween - expectedManagersBetween;
    }

    public int getExpectedManagersBetween() {
        return expectedManagersBetween;
    }
}
