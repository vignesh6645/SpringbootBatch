package com.example.demo.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/batch")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    private final String STORAGE="C://Batch_Storage/";

    @PostMapping("/save")
    private String Save(@RequestParam(value = "file")MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        File inputFile =new File(STORAGE+fileName);
        file.transferTo(inputFile);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath",STORAGE+fileName)
                .addLong("startAt",System.currentTimeMillis()).toJobParameters();
        try {
      JobExecution execution= jobLauncher.run(job,jobParameters);

      if (execution.getExitStatus().equals(ExitStatus.COMPLETED)){
          Files.deleteIfExists(Paths.get(STORAGE + fileName));
      }

           return "File Upload SuccessFully.";

        } catch (JobExecutionAlreadyRunningException |
                JobParametersInvalidException | JobInstanceAlreadyCompleteException |
                JobRestartException e) {

            e.printStackTrace();
            return null;
        }

    }

}
