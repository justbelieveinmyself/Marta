package com.justbelieveinmyself.marta.configs.beans;

import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class FileHelper {
    @Value("${upload.path}")
    private String uploadPath;
    public String uploadFile(MultipartFile file, UploadDirectory to) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String filename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + to.getPath() + "/" + filename));
            return filename;
        }
        return null;
    }
    public ResponseEntity<?> downloadFile(String filename, UploadDirectory from) {
        String uploadPath = "C:/users/shadow/IdeaProjects/Marta/uploads";
        try {
            Path filePath = Paths.get(uploadPath + "/" + from.getPath() + "/" + filename);
            if (!Files.exists(filePath)) {
                throw new IOException("File not found");
            }
            Resource resource = new UrlResource(filePath.toUri());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("File-Name", filename);
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .headers(httpHeaders).body(resource);
        }
        catch (IOException exception){
            return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), "File not found!")
                    , HttpStatus.NOT_FOUND);
        }
    }
    public byte[] downloadFileAsByteArray(String filename, UploadDirectory from) {
        String uploadPath = "C:/users/shadow/IdeaProjects/Marta/uploads";
        try {
            Path filePath = Paths.get(uploadPath + "/" + from.getPath() + "/" + filename);
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found");
            }
            Resource resource = new UrlResource(filePath.toUri());
            if(!resource.exists()) {
                throw new FileNotFoundException("File not found");
            }
            return resource.getContentAsByteArray();
        }
        catch (IOException exception){
            return "".getBytes();
        }
    }
}