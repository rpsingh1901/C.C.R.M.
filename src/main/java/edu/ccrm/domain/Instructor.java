package edu.ccrm.domain;

public class Instructor extends Person {
    private final String employeeId;
    private String department;

    public Instructor(String employeeId, Name fullName, String email, boolean active, String department) {
        super(fullName, email, active);
        this.employeeId = employeeId;
        this.department = department;
    }

    public String getEmployeeId() { return employeeId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override public String getDisplayId() { return employeeId; }
}
