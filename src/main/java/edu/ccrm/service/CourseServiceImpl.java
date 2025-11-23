package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;

import java.util.*;
import java.util.stream.Stream;

public class CourseServiceImpl implements CourseService {
    private final DataStore store;
    public CourseServiceImpl(DataStore store){ this.store = store; }

    @Override public void addCourse(Course course) { store.getCourses().put(course.getCode(), course); }
    @Override public List<Course> listCourses() { return new ArrayList<Course>(store.getCourses().values()); }
    @Override public Optional<Course> findByCode(String code) { return Optional.ofNullable(store.getCourses().get(code)); }
    @Override public boolean deactivateCourse(String code) {
        Course c = store.getCourses().get(code);
        if (c == null) return false; c.setActive(false); return true;
    }

    @Override public Stream<Course> search(Optional<String> byDepartment, Optional<Semester> bySemester, Optional<String> byInstructorId) {
        Stream<Course> st = store.getCourses().values().stream();
        if (byDepartment.isPresent()) {
            String d = byDepartment.get();
            st = st.filter(c -> d.equalsIgnoreCase(c.getDepartment()));
        }
        if (bySemester.isPresent()) {
            Semester s = bySemester.get();
            st = st.filter(c -> s == c.getSemester());
        }
        if (byInstructorId.isPresent()) {
            String i = byInstructorId.get();
            st = st.filter(c -> i.equalsIgnoreCase(c.getInstructorId()));
        }
        return st;
    }
}
