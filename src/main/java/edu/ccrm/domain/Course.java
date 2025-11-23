package edu.ccrm.domain;

import java.util.Objects;

public class Course {
    private final String code;
    private final String title;
    private final int credits; // assert 1..6
    private final String department;
    private final Semester semester;
    private boolean active = true;
    private String instructorId; // optional

    // Builder pattern
    public static class Builder {
        private final String code;
        private final String title;
        private final int credits;
        private String department = "GENERAL";
        private Semester semester = Semester.FALL;
        private String instructorId;
        public Builder(String code, String title, int credits) { this.code = code; this.title = title; this.credits = credits; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder instructorId(String instructorId) { this.instructorId = instructorId; return this; }
        public Course build() { return new Course(this); }
    }

    // Static nested class demo
    public static class DepartmentInfo { public final String name; public DepartmentInfo(String n){ this.name=n; } }
    // Inner class demo
    public class Roster { public String describe(){ return "Roster for "+code+" ("+title+")"; } }

    private Course(Builder b) {
        assert b.credits >= 1 && b.credits <= 6 : "Credits must be 1..6";
        this.code = b.code;
        this.title = b.title;
        this.credits = b.credits;
        this.department = b.department;
        this.semester = b.semester;
        this.instructorId = b.instructorId;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getDepartment() { return department; }
    public Semester getSemester() { return semester; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    @Override public String toString() { return String.format("Course[%s] %s, %d cr, %s, %s, active=%s", code, title, credits, department, semester, active); }
}
