
package in.onehealth.demo.report.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ReportFileValidator {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a file to upload"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "File size must not exceed 10 MB"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null
                || (!contentType.equals("application/pdf")
                && !contentType.equals("image/jpeg")
                && !contentType.equals("image/png"))) {

            throw new IllegalArgumentException(
                    "Only PDF, JPEG, and PNG files are allowed"
            );
        }
    }
}
