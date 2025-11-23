package edu.ccrm.service;

import edu.ccrm.domain.Semester;
import edu.ccrm.service.exceptions.DuplicateEnrollmentException;
import edu.ccrm.service.exceptions.MaxCreditLimitExceededException;

import java.util.UUID;

public interface EnrollmentService {
    void enroll(UUID studentId, String courseCode, Semester semester) throws DuplicateEnrollmentException, MaxCreditLimitExceededException;
    boolean unenroll(UUID studentId, String courseCode);
    void recordMarks(UUID studentId, String courseCode, int marks);
}
