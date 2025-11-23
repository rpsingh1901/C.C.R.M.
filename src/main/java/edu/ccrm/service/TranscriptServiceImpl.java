package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Transcript;

import java.util.UUID;

public class TranscriptServiceImpl implements TranscriptService {
    private final DataStore store;
    public TranscriptServiceImpl(DataStore store){ this.store = store; }

    @Override public Transcript generateTranscript(UUID studentId) {
        Student s = store.getStudents().get(studentId);
        if (s == null) throw new IllegalArgumentException("Student not found");
        Transcript.Builder b = new Transcript.Builder().student(s);
        for (Enrollment e : store.getEnrollments()) {
            if (e.getStudentId().equals(studentId)) {
                b.add(e);
            }
        }
        return b.build();
    }
}
