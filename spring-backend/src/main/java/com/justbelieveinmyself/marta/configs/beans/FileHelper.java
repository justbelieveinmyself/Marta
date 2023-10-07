package com.justbelieveinmyself.marta.configs.beans;

import com.justbelieveinmyself.marta.domain.UploadTo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class FileHelper {
    @Value("${upload.path}")
    private String uploadPath;
    public String uploadFile(MultipartFile file, UploadTo to) throws IOException {
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
}
