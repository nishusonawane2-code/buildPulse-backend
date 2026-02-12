package com.nisha.construction.lead.dto;

import com.nisha.construction.project.enums.ProjectCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLeadRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "City is required")
    private String city;

    private String source; // ESTIMATOR, CALL, WEBSITE

    private ProjectCategory category;
}
