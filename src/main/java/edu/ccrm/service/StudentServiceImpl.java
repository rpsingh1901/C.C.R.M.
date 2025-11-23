package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Name;
import edu.ccrm.domain.Student;

import java.time.LocalDate;
import java.util.*;

public class StudentServiceImpl implements StudentService {
    private final DataStore store;
    public StudentServiceImpl(DataStore store){ this.store = store; }

    @Override public Student addStudent(String regNo, Name fullName, String email, boolean active, LocalDate enrolledOn) {
        Student s = new Student(regNo, fullName, email, active, enrolledOn);
        store.getStudents().put(s.getId(), s);
        store.getStudentIdByRegNo().put(regNo, s.getId());
        return s;
        // operator precedence note (documented): a + b * c evaluates * before +
    }

    @Override public List<Student> listStudents() { return new ArrayList<Student>(store.getStudents().values()); }

    @Override public Optional<Student> findByRegNo(String regNo) {
        UUID id = store.getStudentIdByRegNo().get(regNo);
        if (id == null) return Optional.<Student>empty();
        return Optional.ofNullable(store.getStudents().get(id));
    }

    @Override public Optional<Student> updateEmailByRegNo(String regNo, String newEmail) {
        Optional<Student> os = findByRegNo(regNo);
        os.ifPresent(s -> s.setEmail(newEmail));
        return os;
    }

    @Override public boolean deactivateByRegNo(String regNo) {
        Optional<Student> os = findByRegNo(regNo);
        os.ifPresent(s -> s.setActive(false));
        return os.isPresent();
    }

    @Override public Optional<Student> findById(UUID id) { return Optional.ofNullable(store.getStudents().get(id)); }
}
