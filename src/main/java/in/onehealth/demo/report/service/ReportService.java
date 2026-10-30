package in.onehealth.demo.report.service;

import in.onehealth.demo.exception.ResourceNotFoundException;
import in.onehealth.demo.patient.entity.Patient;
import in.onehealth.demo.patient.repository.PatientRepository;
import in.onehealth.demo.report.dto.ReportDownloadResponse;
import in.onehealth.demo.report.entity.Report;
import in.onehealth.demo.report.repository.ReportRepository;
import in.onehealth.demo.report.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final PatientRepository patientRepository;
    private final FileStorageService fileStorageService;
    private final ReportFileValidator reportFileValidator;

    public ReportService(ReportRepository reportRepository,
                         PatientRepository patientRepository,
                         FileStorageService fileStorageService,
                         ReportFileValidator reportFileValidator) {
        this.reportRepository = reportRepository;
        this.patientRepository = patientRepository;
        this.fileStorageService = fileStorageService;
        this.reportFileValidator = reportFileValidator;
    }

    public Report uploadReportForUser(
            java.util.UUID userPublicId,
            String title,
            String description,
            MultipartFile file) throws Exception {

        Patient patient = patientRepository
                .findByUser_PublicId(userPublicId)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found"));
        reportFileValidator.validateFile(file);
        String storageKey = fileStorageService.uploadFile(file);

        Report report = new Report();
        report.setTitle(title);
        report.setDescription(description);
        report.setOriginalFileName(file.getOriginalFilename());
        report.setStorageKey(storageKey);
        report.setContentType(file.getContentType());
        report.setFileSize(file.getSize());
        report.setPatient(patient);

        return reportRepository.save(report);
    }

    public List<Report> getMyReports(UUID userPublicId) {
        Patient patient = patientRepository
                .findByUser_PublicId(userPublicId)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found"));

        List<Report> reports =
                reportRepository.findByPatient_Id(patient.getId());

        return reports;
    }



    public ReportDownloadResponse downloadMyReport(
            UUID userPublicId,
            Long reportId) throws Exception {

        // Find the logged-in patient's profile
        Patient patient = patientRepository
                .findByUser_PublicId(userPublicId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        ));

        // Find the report and verify that this patient owns it
        Report report = reportRepository
                .findByIdAndPatient_Id(reportId, patient.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Report not found"
                        ));

        // Download the actual file from MinIO
        InputStream inputStream = fileStorageService.downloadFile(
                report.getStorageKey()
        );

        // Return the report details and the file together
        ReportDownloadResponse response =
                new ReportDownloadResponse(report, inputStream);

        return response;
    }


}
