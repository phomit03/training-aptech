import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static utils.EmployeeUtils.*;

public class Main {
    public static void main(String[] args) {
        LinkedHashMap<String, Employee> employees = new LinkedHashMap<>();

        //read data
        readData(employees);

        while (true) {
            System.out.println("\n===== Employee Salary Management =====");
            System.out.println("1. Add employee information");
            System.out.println("2. Edit employee information");
            System.out.println("3. Delete employee information");
            System.out.println("4. Search salary information");
            System.out.println("5. Display salary information");
            System.out.println("0. Exit");
            System.out.println("=====================================\n");

            int choice = getIntInput("Choose an action: ");

            switch (choice) {
                case 1:
                    addEmployeeInfo(employees);
                    break;
                case 2:
                    editEmployeeInfo(employees);
                    break;
                case 3:
                    deleteEmployeeInfo(employees);
                    break;
                case 4:
                    searchEmployeeInfo(employees);
                    break;
                case 5:
                    displayAllEmployeeInfo(employees);
                    break;
                case 0:
                    System.out.println("Notification: Program End!");
                    System.exit(0);     //close
                    break;
                default:
                    System.out.println("Notification: Invalid choice. Please choose again.");
            }
        }
    }

    private static void addEmployeeInfo(LinkedHashMap<String, Employee> employees) {
        int numberOfEmployee = getIntInput("\nEnter the number of employees in the factory: ");

        for (int i = 0; i < numberOfEmployee; i++) {
            System.out.println("\nEnter information for employee number " + (i + 1) + ":");

            String code = generateEmployeeCode(employees);

            String name = getStringInput("Employee name: ");

            int choosePosition = getIntInput("Position (1: Manager / 2: Full time staff / 3: Part time staff): ");
            String position = getPosition(choosePosition);

            assert position != null;
            int overtime = getOvertimeByPosition(position);

            if (position.equals("Manager") || position.equals("Full time staff")) {
                Employee fullTimeEmployee = new FullTimeEmployee(position, overtime);
                fullTimeEmployee.setCode(code);
                fullTimeEmployee.setName(name);
                fullTimeEmployee.calculateSalary();
                employees.put(code, fullTimeEmployee);
            } else {
                Employee partTimeEmployee = new PartTimeEmployee(position, overtime);
                partTimeEmployee.setCode(code);
                partTimeEmployee.setName(name);
                partTimeEmployee.calculateSalary();
                employees.put(code, partTimeEmployee);
            }

            System.out.println("\nSuccess: Employee number " + (i + 1) + " added successfully!");
        }

        System.out.println("\nNotification: Employee information entry complete.\n");

        //save data
        writeData(employees);

        //Show list employee
        displayAllEmployeeInfo(employees);
    }

    private static void editEmployeeInfo(LinkedHashMap<String, Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNotification: No employee information available.");
            return;
        }

        //Show list employee
        displayAllEmployeeInfo(employees);

        String employeeCode = getStringInput("\nEnter the employee code to edit: ");
        Employee employee = employees.get(employeeCode);

        if (employee != null) {
            if(employee.getCode().equals(employeeCode)){
                System.out.println("\nEnter new information:");

                String newName = getStringInput("New name: ");

                int choosePosition = getIntInput("Position (1: Manager / 2: Full time staff / 3: Part time staff): ");
                String newPosition = getPosition(choosePosition);

                assert newPosition != null;
                int newOvertime = getOvertimeByPosition(newPosition);

                if (employee instanceof FullTimeEmployee) {
                    employee.setName(newName);
                    ((FullTimeEmployee) employee).setPosition(newPosition);
                    ((FullTimeEmployee) employee).setOvertime(newOvertime);
                    employee.calculateSalary();
                }
                if (employee instanceof PartTimeEmployee) {
                    employee.setName(newName);
                    ((PartTimeEmployee) employee).setPosition(newPosition);
                    ((PartTimeEmployee) employee).setOvertime(newOvertime);
                    employee.calculateSalary();
                }

                System.out.println("\nSuccess: Employee information with code '" + employeeCode + "' updated successfully!");
            }
        } else {
            System.out.println("Notification: No employee found with code '" + employeeCode + "'!");
        }

        //save data
        writeData(employees);

        //Show list employee
        displayAllEmployeeInfo(employees);
    }

    private static void deleteEmployeeInfo(LinkedHashMap<String, Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNotification: No employee information available.");
            return;
        }
        //Show list employee
        displayAllEmployeeInfo(employees);

        String employeeCode = getStringInput("\nEnter the employee code to delete: ");
        Employee employee = employees.get(employeeCode);

        if (employee != null) {
            if(employee.getCode().equals(employeeCode)){
                employees.remove(employeeCode);
                System.out.println("\nSuccess: Employee information with code '" + employeeCode + "' deleted successfully!");
            }
        } else {
            System.out.println("Notification: No employee found with code '" + employeeCode + "'!");
        }

        //save data
        writeData(employees);
    }

    private static void searchEmployeeInfo(LinkedHashMap<String, Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("Notification: No employee information available.");
            return;
        }

        //Show list employee
        displayAllEmployeeInfo(employees);

        String nameToSearch = getStringInput("\nEnter the name of the employee to search: ");
        String monthToSearch = getStringInput("Enter the month to search (yyyyMM): ");

        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.printf("%-20s%-25s%-25s%-15s%-15s\n", "Employee Code", "Name", "Position", "Overtime", "Salary");
        System.out.println("------------------|------------------------|------------------------|--------------|------------");

        boolean checkNotFound = false;
        for (Employee employee : employees.values()) {
            //name duoc so sanh = contains thay vi equal (%s%)
            if (employee.getName().contains(nameToSearch) && monthToSearch.equals(getFormattedFile())) {
                employee.displayInfo();
                checkNotFound = true;
            }
        }

        if(!checkNotFound) {
            System.out.println("Notification: No employee found with name '" + nameToSearch + "' for month '" + monthToSearch + "'!");
        }
    }

    private static void displayAllEmployeeInfo(LinkedHashMap<String, Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNotification: No employee information available.");
        } else {
            System.out.println("\n=================================== Employee Information ===================================");

            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.printf("%-20s%-25s%-25s%-15s%-15s\n", "Employee Code", "Name", "Position", "Overtime", "Salary");
            System.out.println("------------------|------------------------|------------------------|--------------|------------");
            for (Employee employee : employees.values()) {
                employee.displayInfo();
            }
        }
    }

}