package edu.ccrm.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();

    private final Path projectRoot;
    private final Path dataFolder;
    private final Path exportFolder;
    private final Path backupFolder;

    private AppConfig() {
        // Assume project root is working dir when running
        this.projectRoot = Paths.get("").toAbsolutePath();
        this.dataFolder = projectRoot.resolve("data");
        this.exportFolder = dataFolder.resolve("export");
        this.backupFolder = dataFolder.resolve("backup");
        try {
            Files.createDirectories(dataFolder);
            Files.createDirectories(exportFolder);
            Files.createDirectories(backupFolder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to init folders", e);
        }
    }

    public static AppConfig getInstance() { return INSTANCE; }

    public Path getProjectRoot() { return projectRoot; }
    public Path getDataFolder() { return dataFolder; }
    public Path getExportFolder() { return exportFolder; }
    public Path getBackupFolder() { return backupFolder; }

    public String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    }
}
