package bible.bible.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Controller
public class ImageController {

    private final Path uploadPath;

    public ImageController(ServletContext servletContext) {
        this.uploadPath = Paths.get(servletContext.getRealPath("/share/")).normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<String> uploadImage(@RequestBody String imageBase64) {
        try {
            if (imageBase64 == null || !imageBase64.startsWith("data:image/png;base64,")) {
                return ResponseEntity.badRequest().body("{\"error\": \"Invalid image data\"}");
            }

            byte[] imageBytes = Base64.getDecoder().decode(imageBase64.substring("data:image/png;base64,".length()));
            String fileName = UUID.randomUUID().toString() + ".png";
            Path destinationFile = this.uploadPath.resolve(Paths.get(fileName)).normalize();

            Files.write(destinationFile, imageBytes);

            String fileUrl = "/share-view/" + fileName;
            return ResponseEntity.ok().body("{\"url\": \"" + fileUrl + "\"}");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Image upload failed\"}");
        }
    }

    @GetMapping("/share-view/{filename:.+}")
    public String shareView(@PathVariable String filename) {
        return "share_view";
    }

    @GetMapping("/share/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = uploadPath.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
