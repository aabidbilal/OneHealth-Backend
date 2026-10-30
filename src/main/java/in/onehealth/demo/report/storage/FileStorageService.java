package in.onehealth.demo.report.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


public interface FileStorageService {

    String uploadFile(MultipartFile file) throws Exception;
    InputStream downloadFile(String fileName) throws Exception;
    void deleteFile(String fileName) throws Exception;

}
