package in.onehealth.demo.doctor.controller;

import in.onehealth.demo.doctor.dto.DoctorRegistrationRequest;
import in.onehealth.demo.doctor.dto.DoctorRegistrationResponse;
import in.onehealth.demo.doctor.entity.DoctorProfile;
import in.onehealth.demo.doctor.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/register")
    public ResponseEntity<DoctorRegistrationResponse> registerDoctor(
            @Valid @RequestBody DoctorRegistrationRequest request) {

        DoctorProfile doctor = doctorService.createDoctor(
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                request.getRegistrationNumber(),
                request.getSpecialization()
        );

        DoctorRegistrationResponse response =
                new DoctorRegistrationResponse(
                        "Doctor registered successfully",
                        doctor.getFullName(),
                        doctor.getUser().getEmail(),
                        doctor.getSpecialization()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}