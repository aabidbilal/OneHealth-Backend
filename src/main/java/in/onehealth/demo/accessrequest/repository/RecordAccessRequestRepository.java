package in.onehealth.demo.accessrequest.repository;

import in.onehealth.demo.accessrequest.entity.AccessRequestStatus;
import in.onehealth.demo.accessrequest.entity.RecordAccessRequest;
import in.onehealth.demo.doctor.entity.DoctorProfile;
import in.onehealth.demo.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordAccessRequestRepository extends JpaRepository<RecordAccessRequest, Long> {

    List<RecordAccessRequest> findByPatientAndStatus(Patient patient,
                                                     AccessRequestStatus status);
    boolean existsByPatientAndDoctorAndStatus(Patient patient,
                                              DoctorProfile doctor,
                                              AccessRequestStatus status);
}
