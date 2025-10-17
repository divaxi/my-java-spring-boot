package com.example.test.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidDTO {

  @NotBlank(message = "First name không được để trống")
  private String firstName;

  @NotBlank(message = "Last name không được để trống")
  private String lastName;
}
