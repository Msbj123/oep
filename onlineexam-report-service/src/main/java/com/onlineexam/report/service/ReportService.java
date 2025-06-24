package com.onlineexam.report.service;

import com.onlineexam.report.dto.ReportSummaryDTO;
import com.onlineexam.report.dto.ResponseSummaryDTO;
import com.onlineexam.report.entity.Report;
import com.onlineexam.report.feignclient.ResponseFeignClient;
import com.onlineexam.report.repository.ReportRepository;
import com.onlineexam.report.exception.ResourceNotFoundException; // Import for consistency

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ResponseFeignClient responseFeignClient; // Autowire the Feign client

    public Report generateReport(Integer userId, Integer examId) {
        // Call the Response Microservice using Feign Client
        ResponseEntity<List<ResponseSummaryDTO>> responseEntity = null;
        try {
            responseEntity = responseFeignClient.getResponsesByUserAndExam(userId, examId);
        } catch (Exception e) {
            // Log the exception and throw a more specific one if needed
            throw new RuntimeException("Error communicating with Response Service: " + e.getMessage(), e);
        }


        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new ResourceNotFoundException("Failed to retrieve responses from Response Service for user " + userId + " and exam " + examId + ". Status: " + responseEntity.getStatusCode());
        }

        List<ResponseSummaryDTO> responses = responseEntity.getBody();

        if (responses.isEmpty()) {
            throw new IllegalArgumentException("No responses found for user " + userId + " and exam " + examId + " from Response Service.");
        }

        int totalMarks = responses.stream()
                .mapToInt(ResponseSummaryDTO::getMarksObtained)
                .sum();

        String performance;
        if (totalMarks > 60) {
            performance = "First Class";
        } else if (totalMarks > 40) {
            performance = "Second Class";
        } else {
            performance = "Fail";
        }

        
       

        Report report = new Report();
        report.setUserId(userId);
        report.setExamId(examId);
        report.setTotalMarks(totalMarks);
        report.setPerformanceMetrics(performance);

        return reportRepository.save(report);
    }


    public List<ReportSummaryDTO> getReportsByExamId(Integer examId) {
        List<Report> reports = reportRepository.findByExamId(examId);
        if (reports.isEmpty()) {
            throw new ResourceNotFoundException("Exam ID not found: " + examId); // Use specific exception
        }
        return reports.stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ReportSummaryDTO> getReportsByUserId(Integer userId) {
        List<Report> reports = reportRepository.findByUserId(userId);
        if (reports.isEmpty()) {
            throw new ResourceNotFoundException("No reports found for user ID: " + userId); // Use specific exception
        }
        return reports.stream()
                .map(this::mapToDto)
                .toList();
    }

    public Optional<ReportSummaryDTO> getReportByUserIdAndExamId(Integer userId, Integer examId) {
        return reportRepository.findByUserIdAndExamId(userId, examId)
                .map(this::mapToDto);
    }

    public List<ReportSummaryDTO> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            throw new ResourceNotFoundException("No reports found"); // Use specific exception
        }
        return reports.stream()
                .map(this::mapToDto)
                .toList();
    }

    // Mapping helper
    private ReportSummaryDTO mapToDto(Report report) {
        return ReportSummaryDTO.builder()
                .reportId(report.getReportId())
                .examId(report.getExamId())
                .userId(report.getUserId())
                .totalMarks(report.getTotalMarks())
                .performanceMetrics(report.getPerformanceMetrics())
                .build();
    }


    public boolean deleteReports(Integer userId, Integer examId) {
        if (userId != null && examId != null) {
            Optional<Report> report = reportRepository.findByUserIdAndExamId(userId, examId);
            if (report.isPresent()) {
                reportRepository.delete(report.get());
                return true;
            } else {
                throw new ResourceNotFoundException("No report found for the given userId and examId."); // Use specific exception
            }
        } else if (userId != null) {
            List<Report> reports = reportRepository.findByUserId(userId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            } else {
                throw new ResourceNotFoundException("No reports found for the given userId."); // Use specific exception
            }
        } else if (examId != null) {
            List<Report> reports = reportRepository.findByExamId(examId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            } else {
                throw new ResourceNotFoundException("No reports found for the given examId."); // Use specific exception
            }
        } else {
            throw new IllegalArgumentException("At least one of userId or examId must be provided.");
        }
    }


    public void deleteAllReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            throw new ResourceNotFoundException("No reports found to delete."); // Use specific exception
        }
        reportRepository.deleteAll(reports);
    }


    public ResponseEntity<ReportSummaryDTO> returnTopper() {
        List<Report> result = reportRepository.findTopperByTotalMarks();

        if (!result.isEmpty()) {
            Report report = result.get(0);
            ReportSummaryDTO dto = ReportSummaryDTO.builder()
                    .reportId(report.getReportId())
                    .examId(report.getExamId())
                    .userId(report.getUserId())
                    .totalMarks(report.getTotalMarks())
                    .performanceMetrics(report.getPerformanceMetrics())
                    .build();

            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }


    public int returnRank(Integer userId) {
        List<Report> userTotals = reportRepository.findTopperByTotalMarks();
        if (!userTotals.isEmpty()) {
            for (int i = 0; i < userTotals.size(); i++) {
                Report report = userTotals.get(i);
                if (report.getUserId().equals(userId)) {
                    return i + 1;
                }
            }
        }
        throw new ResourceNotFoundException("No reports found for the given userId to determine rank."); // Use specific exception
    }
}
