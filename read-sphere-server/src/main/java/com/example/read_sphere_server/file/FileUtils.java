package com.example.read_sphere_server.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {
    public static byte[] readFileFromLocation(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }

        try {
            Path filepath = new File(fileUrl).toPath();
            return Files.readAllBytes(filepath);
        } catch (IOException e) {
            log.warn("File not found in path {} ", fileUrl);
        }

        return new byte[0];
    }
}
