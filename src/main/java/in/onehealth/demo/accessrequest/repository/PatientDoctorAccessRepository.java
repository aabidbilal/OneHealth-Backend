package in.onehealth.demo.accessrequest.repository;

import in.onehealth.demo.accessrequest.entity.PatientDoctorAccess;
import in.onehealth.demo.doctor.entity.DoctorProfile;
import in.onehealth.demo.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientDoctorAccessRepository extends JpaRepository<PatientDoctorAccess, Long> {

    // Check whether a doctor currently has access to a patient
    Optional<PatientDoctorAccess> findByPatientAndDoctorAndRevokedAtIsNull(
            Patient patient,
            DoctorProfile doctor
    );
    // Find all active doctors authorized by a patient
    List<PatientDoctorAccess> findByPatientAndRevokedAtIsNull(
            Patient patient
    );
    // Find all active patient authorized by a doctor
    List<PatientDoctorAccess> findByDoctorAndRevokedAtIsNull(
            DoctorProfile doctor
    );
}
