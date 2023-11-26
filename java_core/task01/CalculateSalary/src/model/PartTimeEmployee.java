package model;

public class PartTimeEmployee extends Employee{
    private String position;
    private int overtime;
    private static long SALARY_STAFF_PART_TIME = 240000;
    private static long SALARY_OVERTIME_HOUR = 50000;

    public PartTimeEmployee() {
        super();
    }

    public PartTimeEmployee(String position, int overtime) {
        super();
        this.position = position;
        this.overtime = overtime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    @Override
    public void calculateSalary() {
        salary = SALARY_STAFF_PART_TIME + (overtime * SALARY_OVERTIME_HOUR);
    }

    @Override
    public void displayInfo() {
        String overtimeFormat;
        overtimeFormat = getOvertime() + " hours";
        System.out.printf("%-20s%-25s%-25s%-15s%-15.0f\n", getCode(), getName(), getPosition(), overtimeFormat, getSalary());
    }
}
