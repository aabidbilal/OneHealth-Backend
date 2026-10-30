package in.onehealth.demo.patient.repository;

import in.onehealth.demo.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByOneHealthId(String oneHealthId);

    boolean existsByOneHealthId(String oneHealthId);

    Optional<Patient> findByUser_PublicId(UUID publicId);

}
