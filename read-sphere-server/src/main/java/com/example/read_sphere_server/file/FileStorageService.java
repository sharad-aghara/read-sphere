package com.example.read_sphere_server.file;

import com.example.read_sphere_server.model.Book;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@Service
@RequiredArgsConstructor
@Slf4j  // to log errors / to log anything
public class FileStorageService {

    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadSubPath;

    public String saveFile(
            @NotNull MultipartFile sourceFile,
            @NotNull Integer userId
    ) {
        final String fileUploadSubPath = "users" + separator + userId;

        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
            @NotNull MultipartFile sourceFile,
            @NotNull String fileUploadSubPath
    ) {
        final String finalUploadPath = fileUploadSubPath + separator +fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create target folder");
                return null;
            }
        }

        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        // ./uploads/users/1/2345673456.jpg
        String targetFilePath = finalUploadPath + separator + System.currentTimeMillis() + "." + fileExtension;

        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved.");
        } catch (Exception e) {
            log.error("File was not saved: " + e);
        }

        return null;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }

        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }
}
