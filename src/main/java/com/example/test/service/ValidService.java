package com.example.test.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.test.dto.ValidDTO;
import com.example.test.utils.User;

import jakarta.validation.executable.ValidateOnExecution;
import lombok.extern.slf4j.Slf4j;

@Service
@ValidateOnExecution
@Slf4j
public class ValidService {

  private Random random = new Random();

  @Value("${app.name:}")
  private String name;

  private final KafkaTemplate<String,String> kafkaTemplate;

  public ValidService (KafkaTemplate<String,String> kafkaTemplate){
    this.kafkaTemplate = kafkaTemplate;
  }

  public List<User> validator(ValidDTO validDTO) {

     List<User> users =User.getSampleUsers();
     return users;
  }

  public String get(){
    return "Only admin can see this.";
  }

  public String send(String msg){
    kafkaTemplate.send("kafka-test",msg);
    return "Message sent";
  }

    @Async  
    public void executeAsyncTask() {
        log.info("Task started in thread: {}", Thread.currentThread().getName());

        try {
            Thread.sleep(5000);  // Simulating a long-running task (5 seconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Task completed in thread: {}", Thread.currentThread().getName());
    }
}

