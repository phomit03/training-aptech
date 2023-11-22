package model;

import java.util.Date;

public class Employee {
    public String code;

    public String name;
    public String position;
    public int overtime;
    public double salary;


    public Employee(String code, String name, String position, int overtime, double salary) {
        this.code = code;
        this.name = name;
        this.position = position;
        this.overtime = overtime;
        this.salary = calculateSalary();
    }

    public double calculateSalary() {
        if (position.equals("Full time staff")) {
            salary = 6000000 + (overtime * 500000);
        } else if (position.equals("Manager")) {
            salary = 10000000 + (overtime * 500000);
        } else {
            salary = 240000 + (overtime * 50000);
        }
        return salary;
    }

    public void displayInfo() {
        String overtimeFormat;
        if (position.equals("Part time staff")) {
            overtimeFormat = overtime + " hours";
        } else {
            overtimeFormat = overtime + " days";
        }
        System.out.printf("%-20s%-25s%-25s%-15s%-15.0f\n", code, name, position, overtimeFormat, salary);
    }

    private static int nextId = 1;
    public static String generateEmployeeId() {
        return "E" + String.format("%03d", nextId++);
    }
}
