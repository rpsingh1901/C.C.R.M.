# Campus Course & Records Manager (CCRM)

A console-based Java SE application to manage Students, Courses, Enrollments, Grades, Transcripts, and File Utilities (import/export/backup). Built to demonstrate core Java language features, OOP pillars, Streams, NIO.2, Date/Time API, interfaces, abstract classes, nested classes, enums, lambdas, recursion, and design patterns (Singleton & Builder).

## How to Run
- Prerequisite: JDK 17+ installed and available on PATH
- Compile:
  - PowerShell:
    - mkdir out
    - javac -d out -encoding UTF-8 $(Get-ChildItem -Recurse src\main\java\*.java | ForEach-Object { $_.FullName })
  - CMD:
    - mkdir out
    - for /R %f in (src\main\java\*.java) do @set files=%files% "%f"
    - javac -d out -encoding UTF-8 %files%
- Run:
  - java -cp out edu.ccrm.cli.Main

## Minimum Demo Flow
1. On start, AppConfig (Singleton) loads config and ensures folders (data/export/backup).
2. CLI switch menu supports: Students, Courses, Enrollment/Grades, Import/Export, Backup & recursive size, Reports, Exit.
3. Perform enrollments, record grades, print transcript (polymorphism, toString()).
4. Export and Backup to timestamped folder (NIO.2 Path/Files.walk/copy).
5. Program prints short platform note (Java SE vs ME vs EE).

## Project Structure (packages)
- edu.ccrm.cli – menu and input loop
- edu.ccrm.domain – Person (abstract), Student, Instructor, Course (Builder, nested/inner), Enrollment, Grade (enum), Semester (enum), Name (immutable), Transcript (Builder)
- edu.ccrm.service – StudentService, CourseService, EnrollmentService, TranscriptService (+ in-memory impls)
- edu.ccrm.io – ImportExportService (CSV), BackupService
- edu.ccrm.util – Validators, Comparators (lambdas), Recursion utilities, Diamond default-method demo
- edu.ccrm.config – AppConfig (Singleton), DataStore (Singleton)

## Java Concepts Demonstrated
- OOP pillars: Encapsulation (private fields + getters/setters), Inheritance (Person → Student/Instructor), Abstraction (abstract Person), Polymorphism (base-type refs, overridden toString())
- Access levels: private/protected/default/public where appropriate
- Inheritance/constructors: use of `super` in subclasses
- Immutability: Name value class with final fields and defensive copying
- Nested classes: Course has a static nested DepartmentInfo and an inner Roster
- Interfaces: StudentService et al.; Search/filter using functional interfaces (Predicate); Diamond problem via Alpha/Beta default methods with explicit override
- Lambdas: Comparators and stream predicates
- Anonymous inner class: simple callback demo in CLI
- Enums: Semester, Grade (with fields/constructors and grade points)
- Advanced: upcast/downcast + instanceof checks (with comments), method overriding & overloading, Builder pattern (Course.Builder, Transcript.Builder), Singleton (AppConfig, DataStore)
- Exceptions: checked/unchecked, try/catch/multi-catch/finally, throw/throws; custom DuplicateEnrollmentException, MaxCreditLimitExceededException; assertions for invariants
- File I/O (NIO.2): Path/Files check/delete/copy/move; Streams to process lines; backup via walk/copy; recursive directory size
- Date/Time API: LocalDate, LocalDateTime for timestamps and folders
- Core language: primitives/objects, operators, precedence (see comments), decisions (if/else/switch), loops (while/do-while/for/foreach), jump controls (break/continue + one labeled break), arrays & Arrays.sort/binarySearch, Strings (substring/split/join/equals/compareTo)

## Evolution of Java (short bullets)
- 1995: Java 1.0 (Write Once, Run Anywhere)
- 1998–2006: Java 2 (J2SE/J2EE/J2ME), collections, Swing
- 2004: Java 5 (generics, enums, annotations)
- 2011: Java 7 (NIO.2)
- 2014: Java 8 (lambdas, streams, date/time)
- 2017+: 9–17 (modules, var, records, pattern matching progress)
- LTS: 8, 11, 17, 21

## Java ME vs SE vs EE (summary)
- Java ME: Embedded/mobile constrained devices; micro editions and profiles.
- Java SE: Core standard edition; desktop/CLI apps, core APIs.
- Java EE/Jakarta EE: Enterprise server-side (JPA, EJB, Servlets, CDI); runs on app servers.

## JDK vs JRE vs JVM
- JVM: Runtime engine executing bytecode (platform-specific implementation)
- JRE: JVM + standard libraries for running apps (no compilers/tools)
- JDK: JRE + developer tools (javac, jar, javadoc, etc.)

Interaction: javac compiles source to bytecode -> JVM executes; JDK provides the compiler; JRE/JVM provide runtime.

## Windows: Install & Configure Java (steps)
1. Download JDK 17+ for Windows (x64) from vendor (Oracle, Microsoft, Adoptium).
2. Install and note path (e.g., C:\Program Files\Java\jdk-17).
3. Set JAVA_HOME and PATH to include %JAVA_HOME%\bin.
4. Verify: `java -version` and `javac -version`.

Include your screenshots in screenshots/:
- java -version
- Eclipse project setup & run
- Program running (menus)
- Exports/Backups folder

## Using Eclipse IDE
- File → New → Java Project → Project name: ccrm; set JRE to 17+
- Add src/main/java to Build Path or configure as source folder
- Create Run Configuration: Main class = edu.ccrm.cli.Main
- Run and capture screenshots

## Assertions
- Enable with: `java -ea -cp out edu.ccrm.cli.Main`
- Invariants asserted: non-null IDs, credit bounds (1..6), etc.

## Usage (CLI quick reference)
- Import data: choose Import/Export → Import Students/Courses
- Manage Students/Courses: Add/List/Update/Deactivate
- Enrollment: Enroll/Unenroll, Record marks, Compute GPA
- Reports: GPA distribution, Top students
- Backup: Export then Backup; show recursive size

## Mapping: syllabus topic → where
- Encapsulation → edu.ccrm.domain.* (private fields + getters/setters)
- Inheritance/Abstraction → Person → Student/Instructor
- Polymorphism → Transcript printing and service interfaces
- Singleton → edu.ccrm.config.AppConfig, DataStore
- Builder → edu.ccrm.domain.Course.Builder, Transcript.Builder
- Enums → edu.ccrm.domain.Grade/Semester
- Exceptions → edu.ccrm.service.exceptions.* and try/catch in CLI
- NIO.2 + Streams → edu.ccrm.io.* (import/export/backup)
- Recursion → edu.ccrm.util.RecursionUtils
- Lambdas/Streams → filtering/searching in services and reports
- Arrays/Arrays.sort → demo in CLI reports
- Assertions → scattered in domain/service constructors

## Screenshots folder
Place your images under screenshots/ with suggested filenames:
- screenshots/01-java-version.png
- screenshots/02-eclipse-setup.png
- screenshots/03-program-running.png
- screenshots/04-exports-backups-structure.png

## Demo video (optional)
Add a link here (YouTube/Drive):
- Demo: <YOUR_LINK_HERE>

## License
Educational sample project.
