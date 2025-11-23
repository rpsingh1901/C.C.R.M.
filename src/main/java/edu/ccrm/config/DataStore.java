package edu.ccrm.config;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Student;

import java.util.*;

public final class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final Map<UUID, Student> studentsById = new LinkedHashMap<UUID, Student>();
    private final Map<String, UUID> studentIdByRegNo = new LinkedHashMap<String, UUID>();
    private final Map<UUID, Instructor> instructorsById = new LinkedHashMap<UUID, Instructor>();
    private final Map<String, Course> coursesByCode = new LinkedHashMap<String, Course>();
    private final List<Enrollment> enrollments = new ArrayList<Enrollment>();

    private DataStore() {}

    public static DataStore getInstance() { return INSTANCE; }

    public Map<UUID, Student> getStudents() { return studentsById; }
    public Map<String, UUID> getStudentIdByRegNo() { return studentIdByRegNo; }
    public Map<UUID, Instructor> getInstructors() { return instructorsById; }
    public Map<String, Course> getCourses() { return coursesByCode; }
    public List<Enrollment> getEnrollments() { return enrollments; }
}
