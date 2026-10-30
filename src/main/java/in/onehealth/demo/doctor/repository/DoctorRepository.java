package in.onehealth.demo.doctor.repository;

import in.onehealth.demo.doctor.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Long> {

    Optional<DoctorProfile> findByUser_PublicId(UUID publicId);

    boolean existsByRegistrationNumber(String registrationNumber);

}
