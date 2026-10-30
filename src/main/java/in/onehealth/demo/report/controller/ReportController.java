package in.onehealth.demo.report.controller;

import in.onehealth.demo.report.dto.ReportDownloadResponse;
import in.onehealth.demo.report.dto.ReportResponse;
import in.onehealth.demo.report.entity.Report;
import in.onehealth.demo.report.service.ReportService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ReportResponse> uploadReport(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false)
            String description,
            @RequestParam("file") MultipartFile file) throws Exception {

        UUID userPublicId = UUID.fromString(jwt.getSubject());

        Report report = reportService.uploadReportForUser(
                userPublicId,
                title,
                description,
                file
        );

        ReportResponse response = new ReportResponse(
                report.getId(),
                report.getTitle(),
                report.getDescription(),
                report.getOriginalFileName(),
                report.getContentType(),
                report.getFileSize(),
                report.getUploadedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<ReportResponse>> getMyReports(
            @AuthenticationPrincipal Jwt jwt) {

        UUID userPublicId = UUID.fromString(jwt.getSubject());

        List<Report> reports = reportService.getMyReports(userPublicId);

        List<ReportResponse> reportResponses = new ArrayList<>();

        for (Report report : reports) {

            ReportResponse response = new ReportResponse(
                    report.getId(),
                    report.getTitle(),
                    report.getDescription(),
                    report.getOriginalFileName(),
                    report.getContentType(),
                    report.getFileSize(),
                    report.getUploadedAt()
            );

            reportResponses.add(response);
        }

        return ResponseEntity.ok(reportResponses);
    }

    @GetMapping("/{reportId}/download")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<InputStreamResource> downloadReport(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reportId) throws Exception {

        UUID userPublicId = UUID.fromString(jwt.getSubject());

        ReportDownloadResponse downloadResponse =
                reportService.downloadMyReport(userPublicId, reportId);

        Report report = downloadResponse.getReport();

        InputStream inputStream = downloadResponse.getInputStream();

        InputStreamResource fileResource =
                new InputStreamResource(inputStream);

        String originalFileName = report.getOriginalFileName();

        if (originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "report-" + reportId;
        }

        String contentType = report.getContentType();

        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + originalFileName + "\""
                )
                .body(fileResource);
    }


}