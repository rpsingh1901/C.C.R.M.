package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface CourseService {
    void addCourse(Course course);
    List<Course> listCourses();
    Optional<Course> findByCode(String code);
    boolean deactivateCourse(String code);

    // Stream API based search
    Stream<Course> search(Optional<String> byDepartment, Optional<Semester> bySemester, Optional<String> byInstructorId);
}
