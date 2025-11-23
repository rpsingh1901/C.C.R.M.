package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Name;
import edu.ccrm.service.exceptions.DuplicateEnrollmentException;
import edu.ccrm.service.exceptions.MaxCreditLimitExceededException;

import java.util.*;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final DataStore store;
    private static final int MAX_CREDITS_PER_SEM = 24;

    public EnrollmentServiceImpl(DataStore store){ this.store = store; }

    @Override public void enroll(UUID studentId, String courseCode, Semester semester) throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        Student s = store.getStudents().get(studentId);
        if (s == null) throw new IllegalArgumentException("Student not found");
        Course c = store.getCourses().get(courseCode);
        if (c == null) throw new IllegalArgumentException("Course not found");

        // duplicate check
        boolean exists = store.getEnrollments().stream().anyMatch(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode));
        if (exists) throw new DuplicateEnrollmentException("Already enrolled");

        // credit limit check
        int current = store.getEnrollments().stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getSemester() == semester)
                .mapToInt(e -> store.getCourses().getOrDefault(e.getCourseCode(), c).getCredits())
                .sum();
        int newTotal = current + c.getCredits();
        if (newTotal > MAX_CREDITS_PER_SEM) throw new MaxCreditLimitExceededException("Exceeds max credits per semester");

        Enrollment e = new Enrollment(studentId, courseCode, semester);
        store.getEnrollments().add(e);
        s.getEnrolledCourses().add(courseCode);
    }

    @Override public boolean unenroll(UUID studentId, String courseCode) {
        Iterator<Enrollment> it = store.getEnrollments().iterator();
        boolean removed = false;
        while (it.hasNext()) {
            Enrollment e = it.next();
            if (e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode)) {
                it.remove();
                removed = true;
            }
        }
        Student s = store.getStudents().get(studentId);
        if (s != null) { s.getEnrolledCourses().remove(courseCode); }
        return removed;
    }

    @Override public void recordMarks(UUID studentId, String courseCode, int marks) {
        for (Enrollment e : store.getEnrollments()) {
            if (e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode)) {
                e.recordMarks(marks);
                return;
            }
        }
        throw new IllegalArgumentException("Enrollment not found");
    }
}
