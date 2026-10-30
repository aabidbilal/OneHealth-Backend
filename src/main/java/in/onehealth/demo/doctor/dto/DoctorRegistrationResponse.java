package in.onehealth.demo.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorRegistrationResponse {

    private String message;
    private String fullName;
    private String email;
    private String specialization;
}