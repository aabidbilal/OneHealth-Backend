package in.onehealth.demo.accessrequest.service;

import in.onehealth.demo.accessrequest.entity.AccessRequestStatus;
import in.onehealth.demo.accessrequest.entity.PatientDoctorAccess;
import in.onehealth.demo.accessrequest.entity.RecordAccessRequest;
import in.onehealth.demo.accessrequest.repository.PatientDoctorAccessRepository;
import in.onehealth.demo.accessrequest.repository.RecordAccessRequestRepository;
import in.onehealth.demo.doctor.entity.DoctorProfile;
import in.onehealth.demo.doctor.repository.DoctorRepository;
import in.onehealth.demo.patient.entity.Patient;
import in.onehealth.demo.patient.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccessRequestService {

    private final RecordAccessRequestRepository requestRepository;
    private final PatientDoctorAccessRepository accessRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AccessRequestService(
            RecordAccessRequestRepository requestRepository,
            PatientDoctorAccessRepository accessRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.requestRepository = requestRepository;
        this.accessRepository = accessRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }
    @Transactional
    public RecordAccessRequest requestAccess(String oneHealthId, UUID doctorPublicId){

        Patient patient = patientRepository.findByOneHealthId(oneHealthId)
                .orElseThrow(
                        ()-> new IllegalArgumentException("Patient not found")
                        );
        DoctorProfile doctor = doctorRepository.findByUser_PublicId(doctorPublicId)
                .orElseThrow(
                        ()-> new IllegalArgumentException("doctor not found")
                );

        boolean alredyAuthorised = accessRepository.findByPatientAndDoctorAndRevokedAtIsNull(patient, doctor)
                .isPresent();

        if(alredyAuthorised){
            throw new IllegalArgumentException("you have already access to the record of this patient");
        }

        boolean alreadyPendingReq = requestRepository.existsByPatientAndDoctorAndStatus(patient,
                doctor,
                AccessRequestStatus.PENDING);

            if(alreadyPendingReq){
                throw new IllegalArgumentException("An access request is already pending for this patient");
            }
        RecordAccessRequest record = new RecordAccessRequest();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setStatus(AccessRequestStatus.PENDING);

        return requestRepository.save(record);
    }

    @Transactional
    public void approveRequest(Long reqId, UUID patientPublicId){

        RecordAccessRequest request = requestRepository.findById(reqId)
                .orElseThrow(
                        ()-> new IllegalArgumentException("Access req not found")
                );

        UUID ownerId = request.getPatient().getUser().getPublicId();
        if(!ownerId.equals(patientPublicId)){
            throw new IllegalArgumentException("you are not authorised to approve this request");
        }

        if(request.getStatus() != AccessRequestStatus.PENDING){
            throw new IllegalArgumentException("this req has already been processed");
        }

        PatientDoctorAccess access = new PatientDoctorAccess();
        access.setPatient(request.getPatient());
        access.setDoctor(request.getDoctor());
        accessRepository.save(access);

        request.setStatus(AccessRequestStatus.APPROVED);
        request.setCreatedAt(LocalDateTime.now());

        requestRepository.save(request);

    }

    @Transactional
    public void rejectRequest(Long requestId, UUID patientPublicId){

        RecordAccessRequest request = requestRepository.findById(requestId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Access request Not found")
                );
        UUID ownerId = request.getPatient().getUser().getPublicId();
        if(!ownerId.equals(patientPublicId)){
            throw new IllegalArgumentException("you are not authorised to reject this request");
        }
        if (request.getStatus() != AccessRequestStatus.PENDING){
            throw new IllegalArgumentException("this req has already been processed");
        }
        request.setStatus(AccessRequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());

        requestRepository.save(request);
    }

    @Transactional
    public void revokeAccess(Long accessId, UUID patientPublicId){
        PatientDoctorAccess access = accessRepository.findById(accessId)
                .orElseThrow(
                        ()-> new IllegalArgumentException("Access record not found")
                );

        UUID ownerId = access.getPatient().getUser().getPublicId();
        if (!ownerId.equals(patientPublicId)) {
            throw new IllegalArgumentException(
                    "You are not authorized to revoke this access");
        }
        if(!access.isActive()){
            throw new IllegalStateException(
                    "Access has already been revoked");
        }
        access.revokeAccess();
        accessRepository.save(access);
    }

}
