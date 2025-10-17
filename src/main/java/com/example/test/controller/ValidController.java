package com.example.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.service.ValidService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RequestMapping("/valid")
@RestController
public class ValidController {

  private final ValidService validService;

  public ValidController(ValidService validService) {
    this.validService = validService;
  }
  @GetMapping()
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<String> get(){
    return ResponseEntity.ok(validService.get());
  
 }

  @ApiResponse
  @GetMapping("/whoami")
  public ResponseEntity<String> getCurrentUser(Authentication authentication) {
    return ResponseEntity.ok("Current user: " + authentication.getName());
  }


  @PostMapping("/send")
  public ResponseEntity<String> send(@Valid @RequestBody String msg){
    return ResponseEntity.ok(validService.send(msg));
  }

  @GetMapping("/async")
  public String startAsyncTask() {
      validService.executeAsyncTask();
      return "Async task started! You can continue doing other work.";
    }
}
