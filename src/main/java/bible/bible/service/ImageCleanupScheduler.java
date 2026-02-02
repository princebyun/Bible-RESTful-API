package bible.bible.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ImageCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ImageCleanupScheduler.class);
    private final Path uploadPath;

    public ImageCleanupScheduler(ServletContext servletContext) {
        this.uploadPath = Paths.get(servletContext.getRealPath("/share/")).normalize();
    }

    //@Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
    public void cleanupImageDirectory() {
        logger.info("Running image cleanup job...");
        if (Files.exists(uploadPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(uploadPath)) {
                for (Path path : directoryStream) {
                    Files.delete(path);
                    logger.info("Deleted file: {}", path);
                }
                logger.info("Image cleanup job finished.");
            } catch (IOException e) {
                logger.error("Error during image cleanup job", e);
            }
        } else {
            logger.info("Image directory not found, skipping cleanup.");
        }
    }
}