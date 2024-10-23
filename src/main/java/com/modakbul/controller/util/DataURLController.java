package com.modakbul.controller.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class DataURLController {
    @Value("${file.path}")
    private String filePath;

    @GetMapping("/upload/{fileName}")
    public UrlResource showImage(@PathVariable("fileName") String fileName) throws MalformedURLException{
        return new UrlResource("file:" + filePath + fileName);
    }
}
