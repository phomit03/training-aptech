package model;

public class FullTimeEmployee extends Employee {
    private String position;
    private int overtime;
    private static long SALARY_MANAGER = 10000000;
    private static long SALARY_STAFF_FULL_TIME = 6000000;
    private static long SALARY_OVERTIME_DAY = 500000;

    public FullTimeEmployee() {
        super();
    }

    public FullTimeEmployee(String position, int overtime) {
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
        if (position.equals("Manager")) {
            salary = SALARY_MANAGER + (overtime * SALARY_OVERTIME_DAY);
        } else  {
            salary = SALARY_STAFF_FULL_TIME + (overtime * SALARY_OVERTIME_DAY);
        }
    }

    @Override
    public void displayInfo() {
        String overtimeFormat;
        overtimeFormat = getOvertime() + " days";
        System.out.printf("%-20s%-25s%-25s%-15s%-15.0f\n", getCode(), getName(), getPosition(), overtimeFormat, getSalary());
    }
}
