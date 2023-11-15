import model.Employee;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    //input
    private static String getStringInput(String input) {
        System.out.print(input);
        return scanner.nextLine();
    }

    private static int getIntInput(String input) {
        while (true) {
            try {
                System.out.print(input);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("please enter integer type!");
            }
        }
    }

    //write - read data
    private static void readData(String fileName, ArrayList<Employee> employees) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String position = data[1];
                int overtime = Integer.parseInt(data[2]);
                Employee employee = new Employee(name, position, overtime);
                employees.add(employee);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist. Creating a new file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeData(String fileName, ArrayList<Employee> employees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee employee : employees) {
                bw.write(String.format("%s,%s,%d\n", employee.name, employee.position, employee.overtime));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //format file name
    private static String getFormattedFile() {
        return new SimpleDateFormat("yyyyMM").format(new Date());
    }

    //main
    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<>();
        String fileName = "./src/data/salary_" + getFormattedFile() + ".txt";

        //read data
        readData(fileName, employees);

        while (true) {
            System.out.println("\n===== Employee Salary Management =====");
            System.out.println("1. Add employee information");
            System.out.println("2. Edit employee information");
            System.out.println("3. Delete employee information");
            System.out.println("4. Search salary information");
            System.out.println("5. Display salary information");
            System.out.println("0. Exit");
            System.out.println("===================================\n");

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
                    //save data
                    writeData(fileName, employees);

                    System.out.println("Program End!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
    }

    private static void addEmployeeInfo(ArrayList<Employee> employees) {
        int numberOfEmployee = getIntInput("\nEnter the number of employees in the factory: ");

        for (int i = 0; i < numberOfEmployee; i++) {
            System.out.println("\nEnter information for employee number " + (i + 1) + ":");

            String name = getStringInput("Employee name: ");
            int choosePosition = getIntInput("Position (1: Manager / 2: Full-time staff / 3: Part-time staff): ");
            String position;
            switch (choosePosition) {
                case 1:
                    position = "Manager";
                    break;
                case 2:
                    position = "Full-time staff";
                    break;
                case 3:
                    position = "Part-time staff";
                    break;
                default:
                    System.out.println("Invalid employee type. Please enter 1 or 2 or 3!");
                    return;
            }
            int overtime = getIntInput("Overtime duration (days or hours): ");

            Employee employee = new Employee(name, position, overtime);
            employees.add(employee);
            System.out.println("\nEmployee number " + (i + 1) + " added successfully.");
        }
        System.out.println("\nEmployee information entry complete.");

    }

    private static void displayAllEmployeeInfo(ArrayList<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNo employee information available.");
        } else {
            System.out.println("------------------------------- Employee Information -------------------------------");
            System.out.printf("%-20s%-25s%-25s%-15s%-15s\n", "Employee Code", "Name", "Position", "Overtime", "Salary");
            for (Employee employee : employees) {
                employee.displayInfo();
            }
        }
    }

    private static void editEmployeeInfo(ArrayList<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNo employee information available.");
            return;
        }

        String employeeCode = getStringInput("\nEnter the employee code to edit: ");
        for (Employee employee : employees) {
            if (employee.code.equals(employeeCode)) {
                System.out.println("\nEnter new information:");

                String newName = getStringInput("New name: ");

                int choosePosition = getIntInput("Position (1: Manager / 2: Full-time staff / " + "3: Part-time staff): ");
                String newPosition;
                switch (choosePosition) {
                    case 1:
                        newPosition = "Manager";
                        break;
                    case 2:
                        newPosition = "Full-time staff";
                        break;
                    case 3:
                        newPosition = "Part-time staff";
                        break;
                    default:
                        System.out.println("Invalid employee type. Please enter 1 or 2 or 3!");
                        return;
                }

                int newOvertime = getIntInput("New overtime duration (days or hours): ");

                employee.name = newName;
                employee.position = newPosition;
                employee.overtime = newOvertime;
                employee.calculateSalary();
                System.out.println("\nEmployee information with code '" + employeeCode + "' updated successfully!");
                return;
            }
        }
        System.out.println("No employee found with code '" + employeeCode + "'.");
    }

    private static void deleteEmployeeInfo(ArrayList<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNo employee information available.");
            return;
        }

        String employeeCode = getStringInput("\nEnter the employee code to delete: ");
        for (Employee employee : employees) {
            if (employee.code.equals(employeeCode)) {
                employees.remove(employee);
                System.out.println("\nEmployee information with code '" + employeeCode + "' deleted successfully!");
                return;
            }
        }
        System.out.println("No employee found with code '" + employeeCode + "'.");
    }

    private static void searchEmployeeInfo(ArrayList<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employee information available.");
            return;
        }

        String nameToSearch = getStringInput("Enter the name of the employee to search: ");
        String monthToSearch = getStringInput("Enter the month to search (yyyyMM): ");

        System.out.printf("%-20s%-25s%-25s%-15s%-15s\n", "Employee Code", "Name", "Position", "Overtime", "Salary");

        for (Employee employee : employees) {
            if (employee.name.equals(nameToSearch) && monthToSearch.equals(getFormattedFile())) {
                employee.displayInfo();
            }
        }

        System.out.println("No employee found with name '" + nameToSearch + "' for month '" + monthToSearch + "'.");
    }

}