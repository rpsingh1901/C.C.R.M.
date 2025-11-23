package edu.ccrm.io;

import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

public class BackupService {
    private final AppConfig config;
    public BackupService(AppConfig config){ this.config = config; }

    public Optional<Path> backupExport() {
        Path exportDir = config.getExportFolder();
        if (!Files.exists(exportDir)) return Optional.<Path>empty();
        Path dest = config.getBackupFolder().resolve("backup-" + config.timestamp());
        try {
            Files.createDirectories(dest);
            try (var stream = Files.walk(exportDir)) {
                stream.forEach(src -> {
                    try {
                        Path rel = exportDir.relativize(src);
                        Path target = dest.resolve(rel);
                        if (Files.isDirectory(src)) {
                            Files.createDirectories(target);
                        } else {
                            Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) { throw new RuntimeException(e); }
                });
            }
            return Optional.<Path>of(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
