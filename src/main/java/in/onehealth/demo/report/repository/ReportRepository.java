package in.onehealth.demo.report.repository;

import in.onehealth.demo.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByPatient_Id(Long patientId);

    Optional<Report> findByIdAndPatient_Id(Long reportId, Long patientId);
}
