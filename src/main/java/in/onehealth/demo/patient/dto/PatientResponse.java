package in.onehealth.demo.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientResponse {

    private String oneHealthId;
    private String name;
    private String email;
}
