package com.example.rasmusportfoliobackend.api;

import com.example.rasmusportfoliobackend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping("/image") //url example to get here: localhost:8080/image?path=/img/sunglasses.jpg
    public ResponseEntity<byte[]> getImage(@RequestParam String path) {
        byte[] imageBytes = imageService.getImageBytes(path);

        HttpHeaders headers = imageService.getImageHeaders(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
