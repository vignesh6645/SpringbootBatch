package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.model.CustomerDto;
import com.example.demo.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/batch")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    private CustomerRepository customerRepository;

    public long getGguId() {
        LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
        Duration duration = Duration.between(start, LocalDateTime.now());
        long seconds = duration.getSeconds();
        long nanos = duration.getNano();
        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificationBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return
                (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificationBitOfTime;
    }

    @PostMapping("/save")
    private String Save(/*@RequestBody(required = false) List<CustomerDto> customerDto*/){

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt",System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job,jobParameters);

          /* customerDto.forEach(customerDtos ->{
               Customer customer = new Customer();

               String obj = String.valueOf(getGguId());
               if(Objects.nonNull(obj)) {
                   customerDtos.setGguid(obj);
                   customerRepository.save(customer);
               }
           } );*/
           return "Success";

        } catch (JobExecutionAlreadyRunningException |
                JobParametersInvalidException | JobInstanceAlreadyCompleteException |
                JobRestartException e) {

            e.printStackTrace();
            return null;
        }

    }

 /*@GetMapping("/get")
    public String get(@RequestBody(required = false) List<CustomerDto> customerDtos){


            customerDtos.forEach(customerDto -> {
                List<Customer> customers = customerRepository.findAll();
                if (customers.size()>0){
                    Customer customer = new Customer();
                     customerDto.setGguid(String.valueOf(getGguId()));
                     customerRepository.save(customer);
                }
            });
            return "Success";

 }*/

}
