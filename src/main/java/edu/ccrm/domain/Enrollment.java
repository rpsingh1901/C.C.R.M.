package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Enrollment {
    private final UUID studentId;
    private final String courseCode;
    private final Semester semester;
    private final LocalDateTime enrolledOn;
    private Integer marks; // nullable until recorded
    private Grade grade;   // nullable until recorded

    public Enrollment(UUID studentId, String courseCode, Semester semester) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
        this.enrolledOn = LocalDateTime.now();
    }

    public UUID getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public Semester getSemester() { return semester; }
    public LocalDateTime getEnrolledOn() { return enrolledOn; }
    public Integer getMarks() { return marks; }
    public Grade getGrade() { return grade; }

    public void recordMarks(int marks) {
        this.marks = marks;
        this.grade = Grade.fromMarks(marks);
    }

    @Override public String toString() {
        return "Enroll{" + courseCode + ", sem=" + semester + ", marks=" + marks + ", grade=" + grade + "}";
    }
}
