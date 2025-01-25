package com.j2ee.socialmedia.controllers.storage;

import com.j2ee.socialmedia.services.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("files")
public class FileStorageController {
    private final StorageService storageService;

    @Autowired
    public FileStorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            System.out.println("Uploaded file: " + file.getOriginalFilename());
            String originalFilename = file.getOriginalFilename();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String newFilename = timestamp + "_" + originalFilename;
            storageService.store(file, newFilename);
            String fileUrl = storageService.generateFileUrl(newFilename);
            if (fileUrl == null || fileUrl.isEmpty()) {
                return ResponseEntity.status(500).body("Failed to generate file URL");
            }
            System.out.println("File URL: " + fileUrl);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
