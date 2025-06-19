package com.example.demo.Service;

import com.example.demo.DTO.ReportSummaryDTO;
import com.example.demo.Entity.Exam;
import com.example.demo.Entity.Report;
import com.example.demo.Entity.Response;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ReportRepository;
import com.example.demo.Repository.ResponseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service //Marks this class as a Spring service component

public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ResponseRepository responseRepository;

    public Report generateReport(Integer userId, Integer examId) {
        List<Response> responses = responseRepository.findByUserIdAndExamExamId(userId, examId);

        int totalMarks = responses.stream()
                .mapToInt(Response::getMarksObtained)
                .sum();
                

        String performance;
        if (totalMarks > 60) {
            performance = "First Class";
        } else if (totalMarks > 40) {
            performance = "Second Class";
        } else {
            performance = "Fail";
        }
        
        User user=new User();
        user.setUserId(userId);
        
        Exam exam=new Exam();
        exam.setExamId(examId);
        
        Report report = new Report();
        report.setUser(user);
        report.setExam(exam);
        report.setTotalMarks(totalMarks);
        report.setPerformanceMetrics(performance);
        
        return reportRepository.save(report);
    }


    public List<ReportSummaryDTO> getReportsByExamId(Integer examId) {
        List<Report> reports = reportRepository.findByExamExamId(examId);
        if (reports.isEmpty()) {
            throw new IllegalArgumentException("Exam ID not found: " + examId);
        }
        return reports.stream()
            .map(this::mapToDto)
            .toList();
    }

    public List<ReportSummaryDTO> getReportsByUserId(Integer userId) {
        List<Report> reports = reportRepository.findByUserUserId(userId);
        if (reports.isEmpty()) {
            throw new IllegalArgumentException("User ID not found: " + userId);
        }
        return reports.stream()
            .map(this::mapToDto)
            .toList();
    }

    public Optional<ReportSummaryDTO> getReportByUserIdAndExamId(Integer userId, Integer examId) {
        return reportRepository.findByUserUserIdAndExamExamId(userId, examId)
            .map(this::mapToDto);
    }

    public List<ReportSummaryDTO> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            throw new IllegalArgumentException("No reports found");
        }
        return reports.stream()
            .map(this::mapToDto)
            .toList();
    }

    // Mapping helper
    private ReportSummaryDTO mapToDto(Report report) {
        return ReportSummaryDTO.builder()
            .reportId(report.getReportId())
            .examId(report.getExam().getExamId())
            .userId(report.getUser().getUserId())
            .totalMarks(report.getTotalMarks())
            .performanceMetrics(report.getPerformanceMetrics())
            .build();
    }

    
    public boolean deleteReports(Integer userId, Integer examId) {
        if (userId != null && examId != null) {
            Optional<Report> report = reportRepository.findByUserUserIdAndExamExamId(userId, examId);
            if (report.isPresent()) {
                reportRepository.delete(report.get());
                return true;
            } else {
                throw new IllegalArgumentException("No report found for the given userId and examId.");
            }
        } else if (userId != null) {
            List<Report> reports = reportRepository.findByUserUserId(userId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            } else {
                throw new IllegalArgumentException("No reports found for the given userId.");
            }
        } else if (examId != null) {
            List<Report> reports = reportRepository.findByExamExamId(examId);
            if (!reports.isEmpty()) {
                reportRepository.deleteAll(reports);
                return true;
            } else {
                throw new IllegalArgumentException("No reports found for the given examId.");
            }
        } else {
            throw new IllegalArgumentException("At least one of userId or examId must be provided.");
        }
    }
    
    
    public void deleteAllReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            throw new IllegalArgumentException("No reports found to delete.");
        }
        reportRepository.deleteAll(reports);
    }


    
    
    
    public ResponseEntity<ReportSummaryDTO> returnTopper() {
        List<Report> result = reportRepository.findTopperByTotalMarks();

        if (!result.isEmpty()) {
            Report report = result.get(0);
            ReportSummaryDTO dto = ReportSummaryDTO.builder()
                .reportId(report.getReportId())
                .examId(report.getExam().getExamId())
                .userId(report.getUser().getUserId())
                .totalMarks(report.getTotalMarks())
                .performanceMetrics(report.getPerformanceMetrics())
                .build();

            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }


    
    /*public int returnRank(Integer userId){
    	List<Report> userTotals = reportRepository.findTopperByTotalMarks();
    	if (!userTotals.isEmpty()) {
    		
    		    for (int i = 0; i < userTotals.size(); i++) {
    		        Object[] entry = userTotals.get(i).;
    		        Long user = (Long) entry[0]; // userId is at index 0
    		        if (user.equals(userId)) {
    		            return i + 1; // rank is index + 1
    		        }
    		    }
    		}
    	 throw new IllegalArgumentException("No reports found for the given userId.");
    }
    */
    public int returnRank(Integer userId) {
        List<Report> userTotals = reportRepository.findTopperByTotalMarks(); // assumed sorted DESC by totalMarks
        if (!userTotals.isEmpty()) {
            for (int i = 0; i < userTotals.size(); i++) {
                Report report = userTotals.get(i);
                if (report.getUser().getUserId().equals(userId)) {
                    return i + 1; // rank is index + 1
                }
            }
        }
        throw new IllegalArgumentException("No reports found for the given userId.");
    }

    /*
    public String exportReportsAsCSV() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            return "No data in reports";
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Report ID,Exam ID,User ID,Total Marks,Performance Metrics\n");
        for (Report report : reports) {
            csvBuilder.append(String.format("%d,%d,%d,%.2f,%s\n",
                report.getReportId(),
                report.getExamId(),
                report.getUserId(),
                report.getTotalMarks(),
                report.getPerformanceMetrics()));
        }
        return csvBuilder.toString();
    }*/
        /*
        public String exportReportsToFile() {
            List<Report> reports = reportRepository.findAll();
            if (reports.isEmpty()) {
                return "No data in reports";
            }

            String filePath = "reports_export.csv";
            try (PrintWriter writer = new PrintWriter(new File(filePath))) {
                writer.println("Report ID,Exam ID,User ID,Total Marks,Performance Metrics");
                for (Report report : reports) {
                    writer.printf("%d,%d,%d,%.2f,%s%n",
                        report.getReportId(),
                        report.getExamId(),
                        report.getUserId(),
                        report.getTotalMarks(),
                        report.getPerformanceMetrics());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Error exporting reports";
            }

            return filePath;
        }


*/


}
