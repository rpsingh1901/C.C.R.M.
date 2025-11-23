package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Transcript {
    private final Student student;
    private final List<Enrollment> enrollments;

    public static class Builder {
        private Student student;
        private final List<Enrollment> enrollments = new ArrayList<Enrollment>();
        public Builder student(Student s){ this.student=s; return this; }
        public Builder add(Enrollment e){ this.enrollments.add(e); return this; }
        public Transcript build(){ return new Transcript(student, enrollments); }
    }

    private Transcript(Student student, List<Enrollment> enrollments){
        this.student = student;
        this.enrollments = new ArrayList<Enrollment>(enrollments);
    }

    public double computeGPA(){
        int totalCredits = 0; int totalPoints = 0;
        for (Enrollment e : enrollments){
            Grade g = e.getGrade();
            if (g == null) continue; // continue demo
            int cr = 3; // assume 3 if not resolved to course; overridden in service in practice
            totalCredits += cr;
            totalPoints += cr * g.getPoints();
        }
        return totalCredits == 0 ? 0.0 : (double) totalPoints / totalCredits;
    }

    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript for ").append(student.getRegNo()).append(" - ").append(student.getFullName()).append("\n");
        for (Enrollment e : enrollments){ sb.append("  ").append(e).append("\n"); }
        sb.append(String.format("GPA: %.2f", computeGPA()));
        return sb.toString();
    }
}
