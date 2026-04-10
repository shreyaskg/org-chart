package com.bigcompany.console;

import com.bigcompany.model.ReportingLineAnomaly;
import com.bigcompany.model.SalaryAnomaly;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportPrinter {
    private static final Logger logger = LoggerFactory.getLogger(ReportPrinter.class);

    public void printReport(List<SalaryAnomaly> salaryAnomalies, List<ReportingLineAnomaly> reportingLineAnomalies) {
        logger.info("=========================================================");
        logger.info("           ORGANIZATIONAL STRUCTURE REPORT               ");
        logger.info("=========================================================");
        
        logger.info("");
        logger.info("--- 1. Managers earning less than required ---");
        boolean hasUnderpaid = false;
        for (SalaryAnomaly anomaly : salaryAnomalies) {
            if (anomaly.getType() == SalaryAnomaly.Type.UNDERPAID) {
                String managerName = anomaly.getManager().getFirstName() + " " + anomaly.getManager().getLastName();
                logger.info(String.format("Manager %-20s (ID: %s) earns %.2f less than the required minimum %.2f",
                            managerName, anomaly.getManager().getId(), anomaly.getDiscrepancy(), anomaly.getExpectedSalary()));
                hasUnderpaid = true;
            }
        }
        if (!hasUnderpaid) {
            logger.info("None detected.");
        }

        logger.info("");
        logger.info("--- 2. Managers earning more than allowed ---");
        boolean hasOverpaid = false;
        for (SalaryAnomaly anomaly : salaryAnomalies) {
            if (anomaly.getType() == SalaryAnomaly.Type.OVERPAID) {
                String managerName = anomaly.getManager().getFirstName() + " " + anomaly.getManager().getLastName();
                logger.info(String.format("Manager %-20s (ID: %s) earns %.2f more than the allowed maximum %.2f",
                            managerName, anomaly.getManager().getId(), anomaly.getDiscrepancy(), anomaly.getExpectedSalary()));
                hasOverpaid = true;
            }
        }
        if (!hasOverpaid) {
            logger.info("None detected.");
        }

        logger.info("");
        logger.info("--- 3. Employees with a reporting line too long ---");
        if (reportingLineAnomalies.isEmpty()) {
            logger.info("None detected.");
        } else {
            for (ReportingLineAnomaly anomaly : reportingLineAnomalies) {
                String employeeName = anomaly.getEmployee().getFirstName() + " " + anomaly.getEmployee().getLastName();
                logger.info(String.format("Employee %-20s (ID: %s) has a reporting line too long by %d manager(s) (Actual depth: %d)",
                        employeeName, anomaly.getEmployee().getId(), anomaly.getDiscrepancy(), anomaly.getActualManagersBetween()));
            }
        }
        
        logger.info("=========================================================");
    }
}
