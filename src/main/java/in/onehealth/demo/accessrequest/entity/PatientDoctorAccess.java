package in.onehealth.demo.accessrequest.entity;

import in.onehealth.demo.doctor.entity.DoctorProfile;
import in.onehealth.demo.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Patient_Doctor_Access")
@Getter
@Setter
@NoArgsConstructor
public class PatientDoctorAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Doctor_id", nullable = false)
    private DoctorProfile doctor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime grantedAt;

    private LocalDateTime revokedAt;

    @PrePersist
    protected void onCreate() {
        grantedAt = LocalDateTime.now();
    }
    public boolean isActive() {
        return revokedAt == null;
    }

    public void revokeAccess() {
        if (revokedAt == null) {
            revokedAt = LocalDateTime.now();
        }

    }

}
