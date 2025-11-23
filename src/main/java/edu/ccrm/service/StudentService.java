package edu.ccrm.service;

import edu.ccrm.domain.Name;
import edu.ccrm.domain.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentService {
    Student addStudent(String regNo, Name fullName, String email, boolean active, LocalDate enrolledOn);
    List<Student> listStudents();
    Optional<Student> findByRegNo(String regNo);
    Optional<Student> updateEmailByRegNo(String regNo, String newEmail);
    boolean deactivateByRegNo(String regNo);
    Optional<Student> findById(UUID id);
}
