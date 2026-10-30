package in.onehealth.demo.patient.controller;


import in.onehealth.demo.patient.dto.PatientResponse;
import in.onehealth.demo.patient.service.PatientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/profile")
    public PatientResponse getMyProfile(
            @AuthenticationPrincipal Jwt jwt) {

        UUID publicId = UUID.fromString(jwt.getSubject());

        return patientService.getMyProfile(publicId);
    }
}
