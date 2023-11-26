package model;

public abstract class Employee {
    private String code;
    private String name;
    protected double salary;

    public Employee() {
        super();
    }

    public Employee(String code, String name, double salary) {
        super();
        this.code = code;
        this.name = name;
        this.salary = salary;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public abstract void calculateSalary();
    public abstract void displayInfo();
}
