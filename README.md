# Organizational Structure Analyzer

This is a lightweight, bare-bones Java SE CLI application built to parse corporate employee data and identify structural and compensation anomalies within an organization. 

It specifically evaluates three primary business rules:
1. **Underpaid Managers:** Managers should earn *at least 20% more* than the average salary of their direct subordinates.
2. **Overpaid Managers:** Managers should earn *no more than 50% more* than the average salary of their direct subordinates.
3. **Deep Hierarchies:** No employee should have more than 4 managers sequentially between themselves and the CEO.

## Prerequisites
- **Java SE 25** 
- **Maven 3.6+** 

## How to Build and Run

To compile the application, navigate to the root directory where this README lives and run normal Maven build commands:

```bash
# Clean the target folder and compile the source code
mvn clean compile
```

To run the application, simply pass a local CSV file to the compiled executable using the `-Dexec.args` argument. 
*(Note: Use the `-q` flag for a quiet Maven output so you only see the clean structural report).*

```bash
mvn -q exec:java -Dexec.mainClass="com.bigcompany.Main" -Dexec.args="src/main/resources/employees.csv"
```
*(If no argument is passed, the exact command falls back to gracefully looking for `src/main/resources/employees.csv` automatically!)*

## Running Tests

The logic for tree-traversal and edge-case mathematics is rigorously automated through JUnit 5. You can execute the test suite via:

```bash
mvn clean test
```


