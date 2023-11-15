package model;

import java.util.Date;

public class Employee {
    public String code;

    public String name;
    public String position;
    public int overtime;
    double salary;


    public Employee(String name, String position, int overtime) {
        this.code = generateEmployeeId();
        this.name = name;
        this.position = position;
        this.overtime = overtime;
        calculateSalary();
    }

    public double calculateSalary() {
        if (position.equals("Full-time staff")) {
            salary = 6000000 + (overtime * 500000);
        } else if (position.equals("Manager")) {
            salary = 10000000 + (overtime * 500000);
        } else {
            salary = 240000 + (overtime * 50000);
        }
        return salary;
    }

    public void displayInfo() {
//        if (position.equals("staff part-time")) {
//            overtime = Integer.parseInt(overtime + " hours");
//        } else {
//            overtime = Integer.parseInt(overtime + " days");
//        }
        System.out.printf("%-20s%-25s%-25s%-15s%-15.0f\n", code, name, position, overtime, salary);
    }

    private static int nextId = 1;
    public static String generateEmployeeId() {
        return "E" + String.format("%03d", nextId++);
    }
}
