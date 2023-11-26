package utils;

import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeUtils {
    private static final int MANAGER = 1;
    private static final int FULL_TIME_STAFF = 2;
    private static final int PART_TIME_STAFF = 3;
    private static final String fileName = "./src/data/salary_" + getFormattedFile() + ".txt";
    private static final Scanner scanner = new Scanner(System.in);

    //get input
    public static String getStringInput(String input) {
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

    public static int getIntInput(String input) {
        while (true) {
            try {
                System.out.print(input);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter integer type!");
            }
        }
    }

    //format file name with "yyyyMM" at the moment
    public static String getFormattedFile() {
        return new SimpleDateFormat("yyyyMM").format(new Date());
    }

    //write - read data
    public static void readData(LinkedHashMap<String, Employee> employees) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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

                if (position.equals("Manager") || position.equals("Full time staff")) {
                    FullTimeEmployee fullTimeEmployee = new FullTimeEmployee(position, overtimeFormat);
                    fullTimeEmployee.setCode(code);
                    fullTimeEmployee.setName(name);
                    fullTimeEmployee.setSalary(salary);
                    employees.put(code, fullTimeEmployee);
                } else {
                    PartTimeEmployee partTimeEmployee = new PartTimeEmployee(position, overtimeFormat);
                    partTimeEmployee.setCode(code);
                    partTimeEmployee.setName(name);
                    partTimeEmployee.setSalary(salary);
                    employees.put(code, partTimeEmployee);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File does not exist. Creating a new file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeData(LinkedHashMap<String, Employee> employees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee employee : employees.values()) {
                String overtimeFormat;
                if (employee instanceof FullTimeEmployee) {
                    overtimeFormat = ((FullTimeEmployee) employee).getOvertime() + " days";
                    bw.write(String.format("%s - %s - %s - %s - %8.0f\n", employee.getCode(),
                            employee.getName(), ((FullTimeEmployee) employee).getPosition(), overtimeFormat, employee.getSalary()));
                }
                if (employee instanceof PartTimeEmployee) {
                    overtimeFormat = ((PartTimeEmployee) employee).getOvertime() + " hours";
                    bw.write(String.format("%s - %s - %s - %s - %8.0f\n", employee.getCode(),
                            employee.getName(), ((PartTimeEmployee) employee).getPosition(), overtimeFormat, employee.getSalary()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateEmployeeCode(LinkedHashMap<String, Employee> employees) {
        if (employees.isEmpty()) {
            return "EMP001";    //Neu list rong, lay id dau tien la "EMP001"
        } else {
            // Lay ra phan tu cuoi cung trong List
            List<String> employeeCodes = new ArrayList<>(employees.keySet());
            String lastEmployeeCode = employeeCodes.get(employeeCodes.size() - 1);

            // Tach chuoi, cat 3 phan tu chuoi dau tien
            int lastEmployeeNumber = Integer.parseInt(lastEmployeeCode.substring(3));

            // code++ va tra ve theo dinh dang
            int newEmployeeNumber = lastEmployeeNumber + 1;
            return "EMP" + String.format("%03d", newEmployeeNumber);
        }
    }

    //get position (contains)
    public static String getPosition(int position) {
        switch (position) {
            case MANAGER:
                return "Manager";
            case FULL_TIME_STAFF:
                return "Full time staff";
            case PART_TIME_STAFF:
                return "Part time staff";
            default:
                System.out.println("\nError: Invalid employee type. Please enter 1 or 2 or 3!");
                return null;
        }
    }

    //overtime by position (check exceeds the prescribed)
    public static int getOvertimeByPosition(String position) {
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

                if (overtime > 64) {    //64h = 8days (8h/1day)
                    System.out.println("\nError: The number of overtime hours exceeds the prescribed limit.");
                } else {
                    break;
                }
            }
        }
        return overtime;
    }
}
