package in.onehealth.demo.doctor.service;

import in.onehealth.demo.doctor.entity.DoctorProfile;
import in.onehealth.demo.doctor.repository.DoctorRepository;
import in.onehealth.demo.entity.Role;
import in.onehealth.demo.entity.User;
import in.onehealth.demo.exception.DuplicateResourceException;
import in.onehealth.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DoctorProfile createDoctor(
            String fullName,
            String email,
            String password,
            String registrationNumber,
            String specialization) {

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }

        if (registrationNumber != null
                && !registrationNumber.isBlank()
                && doctorRepository.existsByRegistrationNumber(
                registrationNumber)) {
            throw new DuplicateResourceException(
                    "Registration number already exists"
            );
        }

        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.DOCTOR);

        User savedUser = userRepository.save(user);

        DoctorProfile doctor = new DoctorProfile();
        doctor.setUser(savedUser);
        doctor.setFullName(fullName);
        doctor.setRegistrationNumber(
                registrationNumber == null
                        || registrationNumber.isBlank()
                        ? null
                        : registrationNumber
        );
        doctor.setSpecialization(specialization);

        return doctorRepository.save(doctor);
    }
}
