import model.Employee;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static model.Employee.generateEmployeeId;

public class Main {
    private static final int MANAGER = 1;
    private static final int FULL_TIME_STAFF = 2;
    private static final int PART_TIME_STAFF = 3;
    private static final String fileName = "./src/data/salary_" + getFormattedFile() + ".txt";
    private static final Scanner scanner = new Scanner(System.in);

    //get input
    private static String getStringInput(String input) {
        String inputCheck;
        do {
            System.out.print(input);
            inputCheck = scanner.nextLine();

            if(inputCheck.isEmpty()){
                System.out.println("\nError: Please enter a non-empty value!");
            }
        } while (inputCheck.isEmpty());

        return inputCheck;
    }

    private static int getIntInput(String input) {
        while (true) {
            try {
                System.out.print(input);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter integer type!");
            }
        }
    }

    //write - read data
    private static void readData(Map<String, Employee> employees) {
        try (BufferedReader br = new BufferedReader(new FileReader(Main.fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" - ");
                String code = data[0];
                String name = data[1];
                String position = data[2];
                String overtime = data[3];
                double salary = Double.parseDouble(data[4]);

                // format overtime (string) -> int
                int overtimeFormat = Integer.parseInt(overtime.split(" ")[0]);  //tach chuoi, lay mang dau tien (la gia tri overtime)

                Employee employee = new Employee(code, name, position, overtimeFormat, salary);
                employees.put(code, employee);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File does not exist. Creating a new file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeData(Map<String, Employee> employees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Main.fileName))) {
            for (Employee employee : employees.values()) {
                String overtimeFormat;
                if (employee.position.equals("Part time staff")) {
                    overtimeFormat = employee.overtime + " hours";
                } else {
                    overtimeFormat = employee.overtime + " days";
                }
                bw.write(String.format("%s - %s - %s - %s - %8.0f\n", employee.code, employee.name, employee.position, overtimeFormat, employee.salary));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //format file name with "yyyyMM" at the moment
    private static String getFormattedFile() {
        return new SimpleDateFormat("yyyyMM").format(new Date());
    }

    //get position (contains)
    private static String getPosition(int position) {
        switch (position) {
            case MANAGER:
                return "Manager";
            case FULL_TIME_STAFF:
                return "Full time staff";
            case PART_TIME_STAFF:
                return "Part time staff";
            default:
                System.out.println("Error: Invalid employee type. Please enter 1 or 2 or 3!");
                return null;
        }
    }

    //overtime by position (check exceeds the prescribed)
    private static int getOvertimeByPosition(String position) {
        int overtime;

        while (true) {
            if (position.equals("Manager") || position.equals("Full time staff")) {
                overtime = getIntInput("Please enter the number of overtime days: ");

                if (overtime > 8) {
                    System.out.println("\nError: The number of overtime days exceeds the prescribed limit.");
                } else {
                    break;  //thoat khoi vong lap
                }
            } else {
                overtime = getIntInput("Please enter overtime hours: ");

                if (overtime > 64) {
                    System.out.println("\nError: The number of overtime hours exceeds the prescribed level.");
                } else {
                    break;
                }
            }
        }
        return overtime;
    }


    //main
    public static void main(String[] args) {
        Map<String, Employee> employees = new LinkedHashMap<>();

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

    private static void addEmployeeInfo(Map<String, Employee> employees) {
        int numberOfEmployee = getIntInput("\nEnter the number of employees in the factory: ");

        for (int i = 0; i < numberOfEmployee; i++) {
            System.out.println("\nEnter information for employee number " + (i + 1) + ":");
            String code = generateEmployeeId();

            String name = getStringInput("Employee name: ");

            int choosePosition = getIntInput("Position (1: Manager / 2: Full time staff / 3: Part time staff): ");
            String position = getPosition(choosePosition);

            int overtime = getOvertimeByPosition(position);

            double salary = 0;

            Employee employee = new Employee(code, name, position, overtime, salary);
            employees.put(code, employee);
            System.out.println("\nSuccess: Employee number " + (i + 1) + " added successfully!");
        }

        System.out.println("\nNotification: Employee information entry complete.\n");

        //save data
        writeData(employees);

        //Show list employee
        displayAllEmployeeInfo(employees);
    }

    private static void editEmployeeInfo(Map<String, Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNotification: No employee information available.");
            return;
        }

        //Show list employee
        displayAllEmployeeInfo(employees);

        String employeeCode = getStringInput("\nEnter the employee code to edit: ");
        Employee employee = employees.get(employeeCode);

        if (employee != null) {
            if(employee.code.equals(employeeCode)){
                System.out.println("\nEnter new information:");

                String newName = getStringInput("New name: ");

                int choosePosition = getIntInput("Position (1: Manager / 2: Full time staff / 3: Part time staff): ");
                String newPosition = getPosition(choosePosition);

                int newOvertime = getOvertimeByPosition(newPosition);

                employee.name = newName;
                employee.position = newPosition;
                employee.overtime = newOvertime;
                employee.calculateSalary();

                System.out.println("\nSuccess: Employee information with code '" + employeeCode + "' updated successfully!");
                return;
            }
        } else {
            System.out.println("Notification: No employee found with code '" + employeeCode + "'!");
        }

        //save data
        writeData(employees);

        //Show list employee
        displayAllEmployeeInfo(employees);
    }

    private static void deleteEmployeeInfo(Map<String, Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\nNotification: No employee information available.");
            return;
        }
        //Show list employee
        displayAllEmployeeInfo(employees);

        String employeeCode = getStringInput("\nEnter the employee code to delete: ");
        Employee employee = employees.get(employeeCode);

        if (employee != null) {
            if(employee.code.equals(employeeCode)){
                employees.remove(employeeCode);
                System.out.println("\nSuccess: Employee information with code '" + employeeCode + "' deleted successfully!");
            }
        } else {
            System.out.println("Notification: No employee found with code '" + employeeCode + "'!");
        }

        //save data
        writeData(employees);
    }

    private static void searchEmployeeInfo(Map<String, Employee> employees) {
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
            if (employee.name.contains(nameToSearch) && monthToSearch.equals(getFormattedFile())) {
                employee.displayInfo();
                checkNotFound = true;
            }
        }

        if(!checkNotFound) {
            System.out.println("Notification: No employee found with name '" + nameToSearch + "' for month '" + monthToSearch + "'!");
        }
    }

    private static void displayAllEmployeeInfo(Map<String, Employee> employees) {
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