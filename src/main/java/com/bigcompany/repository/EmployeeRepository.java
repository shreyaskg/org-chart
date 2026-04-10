package com.bigcompany.repository;

import com.bigcompany.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmployeeRepository {
    private final Map<String, Employee> employeesById = new HashMap<>();
    private final Map<String, List<Employee>> subordinatesByManagerId = new HashMap<>();

    public void addEmployee(Employee employee) {
        employeesById.put(employee.getId(), employee);

        employee.getManagerId().ifPresent(managerId -> {
            subordinatesByManagerId
                    .computeIfAbsent(managerId, k -> new ArrayList<>())
                    .add(employee);
        });
    }

    public void addAll(List<Employee> employees) {
        employees.forEach(this::addEmployee);
    }

    public Optional<Employee> getEmployee(String id) {
        return Optional.ofNullable(employeesById.get(id));
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeesById.values());
    }

    public List<Employee> getSubordinates(String managerId) {
        return subordinatesByManagerId.getOrDefault(managerId, Collections.emptyList());
    }
}
