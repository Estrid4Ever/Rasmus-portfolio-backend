package com.example.rasmusportfoliobackend.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ImageService {

    public String convertImagePath(MultipartFile file){
        return "/image?path=/img/" + file.getOriginalFilename();
    }


    public String handleFileUploadTARGET(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Hämta sökvägen till mappen "img" under "resources"
                String uploadDir = new ClassPathResource("img").getFile().getAbsolutePath();

                // Skapa filvägen för den sparade filen
                String filePath = uploadDir + File.separator + file.getOriginalFilename();

                // Skapa en ny File-instans för den sparade filen
                File dest = new File(filePath);

                // Överför innehållet i den uppladdade filen till destinationsfilen
                file.transferTo(dest);

                // Returnera den sparade filens filväg
                return filePath;
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to upload file";
            }
        } else {
            return "File is empty";
        }
    }
    public HttpHeaders getImageHeaders(int imageBytesLength) {
        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytesLength);
        //headers.setCacheControl("no-store"); // Prevent caching

        return headers;
    }

    public byte[] getImageBytes(String path) {
        // Load the image file
        Resource resource = new ClassPathResource(path);
        byte[] imageBytes;
        try {
            imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imageBytes;
    }
}
