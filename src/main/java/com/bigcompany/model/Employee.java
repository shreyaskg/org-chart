package com.bigcompany.model;

import java.util.Objects;
import java.util.Optional;

public class Employee {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private final String managerId;

    public Employee(String id, String firstName, String lastName, double salary, String managerId) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId == null || managerId.trim().isEmpty() ? null : managerId.trim();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSalary() {
        return salary;
    }

    public Optional<String> getManagerId() {
        return Optional.ofNullable(managerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id.equals(employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId='" + managerId + '\'' +
                '}';
    }
}
