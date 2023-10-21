package com.justbelieveinmyself.marta.configs.beans;

import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileHelper {
    @Value("${upload.path}")
    private String uploadPath;
    public String uploadFile(MultipartFile file, UploadDirectory to) {
        try{
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
        }catch (IOException e) {
            throw new NotCreatedException("Cannot upload image!");
        }
    }
    public List<String> uploadFile(MultipartFile[] files, UploadDirectory to) {
        List<String> paths = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            paths.add(uploadFile(file, to));
        });
        return paths;
    }
    public ResponseEntity<?> downloadFileAsResponse(String filename, UploadDirectory from) {
        try {
            UrlResource resource = getFileAsResource(filename, from);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("File-Name", filename);
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(resource.getFile().toPath())))
                    .headers(httpHeaders).body(resource);
        }
        catch (IOException exception){
            return null;
        }
    }

    public UrlResource getFileAsResource(String filename, UploadDirectory from) {
        try {

            Path filePath = Paths.get(uploadPath + "/" + from.getPath() + "/" + filename);
            if (!Files.exists(filePath)) {
                throw new IOException("File not found");
            }
            return new UrlResource(filePath.toUri());
        }
        catch (IOException e){
            return null;
        }
    }

    public byte[] downloadFileAsByteArray(String filename, UploadDirectory from) {
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