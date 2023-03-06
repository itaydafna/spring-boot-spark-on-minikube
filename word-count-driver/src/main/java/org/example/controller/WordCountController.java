package org.example.controller;

import org.apache.spark.sql.SparkSession;
import org.example.WordCountSparkJob;
import org.example.service.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class WordCountController {

    private final SparkService sparkService;

    @Autowired
    public WordCountController(SparkService sparkService) {
        this.sparkService = sparkService;
    }

    @PostMapping("/word-count")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        SparkSession sparkSession = sparkService.getSparkSession();
        byte[] byteArray = file.getBytes();
        String contents = new String(byteArray, StandardCharsets.UTF_8);
        String wordCountOutput = WordCountSparkJob.getWordCount(contents, sparkSession);
        // sparkSession.close();
        return ResponseEntity.ok(wordCountOutput);
    }
}



