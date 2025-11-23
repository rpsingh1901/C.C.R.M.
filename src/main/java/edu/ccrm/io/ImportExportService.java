package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.StudentService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ImportExportService {
    private final AppConfig config;

    public ImportExportService(AppConfig config){ this.config = config; }

    public int importStudents(Path file, StudentService studentService) {
        AtomicInteger count = new AtomicInteger();
        try {
            if (!Files.exists(file)) return 0;
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith("#") || line.toLowerCase().startsWith("regno")) continue;
                String[] p = line.split(",");
                String reg = p[0].trim();
                String full = p.length>1?p[1].trim():"";
                String email = p.length>2?p[2].trim():"";
                boolean active = p.length>3?Boolean.parseBoolean(p[3].trim()):true;
                studentService.addStudent(reg, Name.parseFull(full), email, active, java.time.LocalDate.now());
                count.incrementAndGet();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return count.get();
    }

    public int importCourses(Path file, CourseService courseService) {
        AtomicInteger count = new AtomicInteger();
        try {
            if (!Files.exists(file)) return 0;
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith("#") || line.toLowerCase().startsWith("code")) continue;
                String[] p = line.split(",");
                String code = p[0].trim();
                String title = p.length>1?p[1].trim():code;
                int credits = p.length>2?Integer.parseInt(p[2].trim()):3;
                String dept = p.length>3?p[3].trim():"GENERAL";
                Semester sem = p.length>4?Semester.valueOf(p[4].trim().toUpperCase()):Semester.FALL;
                Course c = new Course.Builder(code, title, credits).department(dept).semester(sem).build();
                courseService.addCourse(c);
                count.incrementAndGet();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return count.get();
    }

    public int exportAll() {
        DataStore store = DataStore.getInstance();
        Path exportDir = config.getExportFolder();
        try {
            Files.createDirectories(exportDir);
            int files = 0;
            // students
            java.util.List<String> sLines = new java.util.ArrayList<String>();
            for (Student s : store.getStudents().values()) {
                sLines.add(String.join(",", s.getRegNo(), s.getFullName().toString(), s.getEmail(), Boolean.toString(s.isActive())));
            }
            Files.write(exportDir.resolve("students.csv"), sLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            files++;
            // courses
            java.util.List<String> cLines = new java.util.ArrayList<String>();
            for (Course c : store.getCourses().values()) {
                cLines.add(String.join(",", c.getCode(), c.getTitle(), Integer.toString(c.getCredits()), c.getDepartment(), c.getSemester().name()));
            }
            Files.write(exportDir.resolve("courses.csv"), cLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            files++;
            // enrollments
            java.util.List<String> eLines = new java.util.ArrayList<String>();
            for (Enrollment e : store.getEnrollments()) {
                eLines.add(String.join(",", e.getStudentId().toString(), e.getCourseCode(), e.getSemester().name(), e.getEnrolledOn().toString(), String.valueOf(e.getMarks()), String.valueOf(e.getGrade())));
            }
            Files.write(exportDir.resolve("enrollments.csv"), eLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            files++;
            return files;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
