package com.batherphilippa.peopledbweb.business.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "First name cannot be empty.")
    private String firstName;
    @NotBlank(message = "Surname cannot be empty.")
    private String lastName;
    @Past(message = "Date of birth must be in the past.")
    @NotNull(message = "Date of birth must be specified.")
    private LocalDate dob;
    @Email(message = "Email must be valid.")
    @NotBlank(message = "Email must not be blank.")
    private String email;
    @DecimalMin(value="800.00", message = "Salary must be at least £800.")
    private BigDecimal salary;
    @NotEmpty
    private String photoFilename;
}
