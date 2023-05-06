package com.example.demo.listener;

import com.example.demo.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.SkipListener;

public class StepSkipListener implements SkipListener<Customer,Number> {
    @Override
    public void onSkipInRead(Throwable throwable) {

    }

    @Override
    public void onSkipInWrite(Number number, Throwable throwable) {

    }

    @Override
    public void onSkipInProcess(Customer customer, Throwable throwable) {
        if (throwable instanceof Exception){
            try {
                throw new Exception("This Item" + new ObjectMapper().writeValueAsString(customer) + " was not saved " +
                        " due to this input : "+ throwable.getMessage(), throwable);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
