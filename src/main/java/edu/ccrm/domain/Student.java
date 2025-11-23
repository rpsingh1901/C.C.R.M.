package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Student extends Person {
    private final String regNo;
    private final LocalDate enrolledOn;
    private final Set<String> enrolledCourses = new HashSet<>(); // course codes

    public Student(String regNo, Name fullName, String email, boolean active, LocalDate enrolledOn) {
        super(fullName, email, active);
        this.regNo = regNo;
        this.enrolledOn = enrolledOn;
    }

    public String getRegNo() { return regNo; }
    public LocalDate getEnrolledOn() { return enrolledOn; }
    public Set<String> getEnrolledCourses() { return enrolledCourses; }

    @Override public String getDisplayId() { return regNo; }

    public String profileString() {
        return String.format("Student[%s] %s | %s | active=%s | since=%s | courses=%s",
                regNo, getFullName(), getEmail(), isActive(), enrolledOn, enrolledCourses);
    }
}
