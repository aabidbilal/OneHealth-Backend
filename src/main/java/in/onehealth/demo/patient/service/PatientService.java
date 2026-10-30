package in.onehealth.demo.patient.service;

import in.onehealth.demo.entity.User;
import in.onehealth.demo.exception.ResourceNotFoundException;
import in.onehealth.demo.patient.dto.PatientResponse;
import in.onehealth.demo.patient.entity.Patient;
import in.onehealth.demo.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatientProfile(User user) {

        Patient patient = new Patient();
        patient.setUser(user);

        patient.setOneHealthId(generateUniqueOneHealthId());

        return patientRepository.save(patient);
    }

    public PatientResponse getMyProfile(UUID publicId) {

        Patient patient = patientRepository
                .findByUser_PublicId(publicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        )
                );

        return new PatientResponse(
                patient.getOneHealthId(),
                patient.getUser().getName(),
                patient.getUser().getEmail()
        );
    }
    private String generateUniqueOneHealthId() {
        String oneHealthId;

        do {
            oneHealthId = "OH-" +
                    UUID.randomUUID().toString()
                            .substring(0, 8)
                            .toUpperCase();
        } while (patientRepository.existsByOneHealthId(oneHealthId));

        return oneHealthId;
    }
}