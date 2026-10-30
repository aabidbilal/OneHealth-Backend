package in.onehealth.demo.report.dto;

import in.onehealth.demo.report.entity.Report;

import java.io.InputStream;

public class ReportDownloadResponse {
    private Report report;
    private InputStream inputStream;

    public ReportDownloadResponse(
            Report report,
            InputStream inputStream) {

        this.report = report;
        this.inputStream = inputStream;
    }

    public Report getReport() {
        return report;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
