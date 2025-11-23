package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.*;

public final class RecursionUtils {
    private RecursionUtils() {}

    // Recursive directory size
    public static long directorySize(Path dir) {
        if (!Files.exists(dir)) return 0L;
        try {
            if (Files.isRegularFile(dir)) return Files.size(dir);
        } catch (IOException e) { return 0L; }
        long total = 0L;
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path p : ds) {
                total += directorySize(p); // recursion
            }
        } catch (IOException e) {
            // ignore for size
        }
        return total;
    }
}
