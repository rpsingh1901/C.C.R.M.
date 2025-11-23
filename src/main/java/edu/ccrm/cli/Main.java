package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import edu.ccrm.io.BackupService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.*;
import edu.ccrm.service.exceptions.DuplicateEnrollmentException;
import edu.ccrm.service.exceptions.MaxCreditLimitExceededException;
import edu.ccrm.util.DiamondDemo;
import edu.ccrm.util.RecursionUtils;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Platform note (SE vs ME vs EE)
        System.out.println("Java SE: standard desktop/CLI APIs | Java ME: embedded/mobile | Java EE/Jakarta: enterprise server-side");

        // Initialize config and datastore (Singletons)
        AppConfig config = AppConfig.getInstance();
        DataStore store = DataStore.getInstance();

        // Services
        StudentService studentService = new StudentServiceImpl(store);
        CourseService courseService = new CourseServiceImpl(store);
        EnrollmentService enrollmentService = new EnrollmentServiceImpl(store);
        TranscriptService transcriptService = new TranscriptServiceImpl(store);

        // Anonymous inner class demo (simple callback/one-off strategy)
        Runnable onStart = new Runnable() { @Override public void run() { System.out.println("CCRM ready. Data folder: " + config.getDataFolder()); }};
        onStart.run();

        // Diamond default-method demo
        new DiamondDemo().printInfo();

        boolean exit = false;
        outer: while (!exit) { // labeled loop for one-time labeled break demo
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": manageStudents(studentService, transcriptService); break;
                case "2": manageCourses(courseService); break;
                case "3": manageEnrollment(studentService, courseService, enrollmentService); break;
                case "4": importExport(config, studentService, courseService); break;
                case "5": backupAndSize(config); break;
                case "6": reports(store); break;
                case "99": System.out.println("Labeled break demo"); break outer; // labeled jump
                case "0": exit = true; break;
                default: System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printMainMenu() {
        System.out.println("\n=== CCRM Main Menu ===");
        System.out.println("1) Manage Students");
        System.out.println("2) Manage Courses");
        System.out.println("3) Enrollment & Grades");
        System.out.println("4) Import/Export Data");
        System.out.println("5) Backup & Show Backup Size (recursive)");
        System.out.println("6) Reports");
        System.out.println("99) Labeled break demo (exit)");
        System.out.println("0) Exit");
        System.out.print("Choose: ");
    }

    private static void manageStudents(StudentService studentService, TranscriptService transcriptService) {
        boolean back = false;
        do {
            System.out.println("\n-- Students --");
            System.out.println("1) Add");
            System.out.println("2) List");
            System.out.println("3) Update email");
            System.out.println("4) Deactivate");
            System.out.println("5) Print profile");
            System.out.println("6) Print transcript");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.print("RegNo: "); String reg = scanner.nextLine();
                    System.out.print("Full Name: "); String fn = scanner.nextLine();
                    System.out.print("Email: "); String em = scanner.nextLine();
                    Student s = studentService.addStudent(reg, Name.parseFull(fn), em, true, LocalDate.now());
                    System.out.println("Added: " + s);
                    break;
                case "2":
                    studentService.listStudents().forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("RegNo: "); String r3 = scanner.nextLine();
                    System.out.print("New Email: "); String e3 = scanner.nextLine();
                    studentService.updateEmailByRegNo(r3, e3).ifPresentOrElse(
                        st -> System.out.println("Updated: " + st),
                        () -> System.out.println("Not found"));
                    break;
                case "4":
                    System.out.print("RegNo: "); String r4 = scanner.nextLine();
                    if (studentService.deactivateByRegNo(r4)) System.out.println("Deactivated."); else System.out.println("Not found.");
                    break;
                case "5":
                    System.out.print("RegNo: "); String r5 = scanner.nextLine();
                    studentService.findByRegNo(r5).ifPresentOrElse(
                        st -> System.out.println(st.profileString()),
                        () -> System.out.println("Not found"));
                    break;
                case "6":
                    System.out.print("RegNo: "); String r6 = scanner.nextLine();
                    studentService.findByRegNo(r6).ifPresentOrElse(st -> {
                        Transcript t = transcriptService.generateTranscript(st.getId());
                        System.out.println(t);
                    }, () -> System.out.println("Not found"));
                    break;
                case "0": back = true; break;
                default: System.out.println("Invalid");
            }
        } while (!back);
    }

    private static void manageCourses(CourseService courseService) {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Courses --");
            System.out.println("1) Add");
            System.out.println("2) List");
            System.out.println("3) Search/Filter");
            System.out.println("4) Deactivate");
            System.out.println("5) Assign instructorId to course");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.print("Code: "); String code = scanner.nextLine();
                    System.out.print("Title: "); String title = scanner.nextLine();
                    System.out.print("Credits (1..6): "); int credits = Integer.parseInt(scanner.nextLine());
                    System.out.print("Department: "); String dept = scanner.nextLine();
                    System.out.print("Semester (SPRING/SUMMER/FALL): "); Semester sem = Semester.valueOf(scanner.nextLine().trim().toUpperCase());
                    Course course = new Course.Builder(code, title, credits)
                            .department(dept)
                            .semester(sem)
                            .build();
                    courseService.addCourse(course);
                    System.out.println("Added: " + course);
                    break;
                case "2":
                    courseService.listCourses().forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("Filter by department (blank to skip): "); String fd = scanner.nextLine();
                    System.out.print("Filter by semester (blank to skip): "); String fs = scanner.nextLine();
                    Optional<Semester> os;
                    if (fs == null || fs.trim().isEmpty()) {
                        os = Optional.<Semester>empty();
                    } else {
                        os = Optional.<Semester>of(Semester.valueOf(fs.trim().toUpperCase()));
                    }
                    Optional<String> od = (fd == null || fd.trim().isEmpty()) ? Optional.<String>empty() : Optional.<String>of(fd.trim());
                    courseService.search(od, os, Optional.<String>empty()).forEach(System.out::println);
                    break;
                case "4":
                    System.out.print("Code: "); String c4 = scanner.nextLine();
                    if (courseService.deactivateCourse(c4)) System.out.println("Deactivated"); else System.out.println("Not found");
                    break;
                case "5":
                    System.out.print("Code: "); String c5 = scanner.nextLine();
                    System.out.print("Instructor ID: "); String iid = scanner.nextLine();
                    courseService.findByCode(c5).ifPresentOrElse(co -> { co.setInstructorId(iid); System.out.println("Assigned."); }, () -> System.out.println("Course not found"));
                    break;
                case "0": back = true; break;
                default: System.out.println("Invalid");
            }
        }
    }

    private static void manageEnrollment(StudentService studentService, CourseService courseService, EnrollmentService enrollmentService) {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Enrollment & Grades --");
            System.out.println("1) Enroll student in course");
            System.out.println("2) Unenroll student from course");
            System.out.println("3) Record marks");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String c = scanner.nextLine().trim();
            try {
                switch (c) {
                    case "1":
                        System.out.print("RegNo: "); String r1 = scanner.nextLine();
                        System.out.print("Course code: "); String cc1 = scanner.nextLine();
                        Optional<Student> os = studentService.findByRegNo(r1);
                        Optional<Course> oc = courseService.findByCode(cc1);
                        if (os.isEmpty() || oc.isEmpty()) { System.out.println("Student or Course not found"); break; }
                        enrollmentService.enroll(os.get().getId(), oc.get().getCode(), oc.get().getSemester());
                        System.out.println("Enrolled.");
                        break;
                    case "2":
                        System.out.print("RegNo: "); String r2 = scanner.nextLine();
                        System.out.print("Course code: "); String cc2 = scanner.nextLine();
                        Optional<Student> os2 = studentService.findByRegNo(r2);
                        if (os2.isEmpty()) { System.out.println("Not found"); break; }
                        if (enrollmentService.unenroll(os2.get().getId(), cc2)) System.out.println("Unenrolled"); else System.out.println("Not enrolled");
                        break;
                    case "3":
                        System.out.print("RegNo: "); String r3 = scanner.nextLine();
                        System.out.print("Course code: "); String cc3 = scanner.nextLine();
                        System.out.print("Marks (0..100): "); int m = Integer.parseInt(scanner.nextLine());
                        Optional<Student> os3 = studentService.findByRegNo(r3);
                        if (os3.isEmpty()) { System.out.println("Not found"); break; }
                        enrollmentService.recordMarks(os3.get().getId(), cc3, m);
                        System.out.println("Recorded.");
                        break;
                    case "0": back = true; break;
                    default: System.out.println("Invalid");
                }
            } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException ex) {
                System.out.println("Business rule: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            } finally {
                // finally demo: runs regardless
            }
        }
    }

    private static void importExport(AppConfig config, StudentService studentService, CourseService courseService) {
        ImportExportService io = new ImportExportService(config);
        System.out.println("\n-- Import/Export --");
        System.out.println("1) Import Students CSV");
        System.out.println("2) Import Courses CSV");
        System.out.println("3) Export all data");
        System.out.print("Choose: ");
        String c = scanner.nextLine().trim();
        switch (c) {
            case "1":
                Path p1 = config.getProjectRoot().resolve("test-data/students.csv");
                int ns = io.importStudents(p1, studentService);
                System.out.println("Imported students: " + ns);
                break;
            case "2":
                Path p2 = config.getProjectRoot().resolve("test-data/courses.csv");
                int nc = io.importCourses(p2, courseService);
                System.out.println("Imported courses: " + nc);
                break;
            case "3":
                int files = io.exportAll();
                System.out.println("Exported files: " + files + " to " + config.getExportFolder());
                break;
            default: System.out.println("Invalid");
        }
    }

    private static void backupAndSize(AppConfig config) {
        BackupService backup = new BackupService(config);
        Optional<Path> dest = backup.backupExport();
        if (dest.isPresent()) {
            System.out.println("Backed up to: " + dest.get());
            long size = RecursionUtils.directorySize(dest.get());
            System.out.println("Recursive backup size: " + size + " bytes");
        } else {
            System.out.println("Nothing to backup. Export first.");
        }
    }

    private static void reports(DataStore store) {
        System.out.println("\n-- Reports --");
        // GPA distribution via Streams
        // GPA distribution
        Map<Grade, Long> dist = new EnumMap<Grade, Long>(Grade.class);
        for (Enrollment e : store.getEnrollments()) {
            Grade g = e.getGrade();
            if (g == null) continue;
            dist.put(g, dist.getOrDefault(g, 0L) + 1L);
        }
        System.out.println("GPA/Grade distribution: " + dist);

        // Arrays demo using course codes
        String[] codes = store.getCourses().keySet().stream().toArray(String[]::new);
        Arrays.sort(codes);
        System.out.println("Sorted course codes: " + Arrays.toString(codes));
        if (codes.length > 0) {
            int idx = Arrays.binarySearch(codes, codes[0]);
            System.out.println("binarySearch found '" + codes[0] + "' at index " + idx);
        }

        // String ops demo
        java.util.List<String> codeList = java.util.Arrays.asList(codes);
        String joined = String.join(",", codeList);
        System.out.println("Joined codes: " + joined);
        if (!joined.isEmpty()) {
            String first = joined.split(",")[0];
            System.out.println("First code via split: " + first + ", compareTo self: " + first.compareTo(first));
        }
    }
}
