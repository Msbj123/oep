package com.example.demo.Controller;

import com.example.demo.DTO.ReportSummaryDTO;
import com.example.demo.Entity.Report;
import com.example.demo.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    
 // Create a new report for a specific user and exam using query parameters
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportSummaryDTO> createReport(@RequestParam Integer userId, @RequestParam Integer examId) {
        Report report = reportService.generateReport(userId, examId);

        ReportSummaryDTO dto = new ReportSummaryDTO(
            report.getReportId(),
            report.getExam().getExamId(),
            report.getUser().getUserId(),
            report.getTotalMarks(),
            report.getPerformanceMetrics()
        );

        return ResponseEntity.ok(dto);
    }

    
    
// Get all reports in the system
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportSummaryDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<List<ReportSummaryDTO>> getReportsByExam(@PathVariable Integer examId) {
        return ResponseEntity.ok(reportService.getReportsByExamId(examId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportSummaryDTO>> getReportsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(reportService.getReportsByUserId(userId));
    }

    @GetMapping("/user/{userId}/exam/{examId}")
    public ResponseEntity<ReportSummaryDTO> getReportByUserAndExam(
            @PathVariable Integer userId,
            @PathVariable Integer examId) {
        return reportService.getReportByUserIdAndExamId(userId, examId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


 // This method handles DELETE requests to delete reports based on optional userId and/or examId.
    
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReports(
        @RequestParam(required = false)  Integer userId,
        @RequestParam(required = false)  Integer examId) {
        
        boolean deleted = reportService.deleteReports(userId, examId);
        if (deleted) {
            return ResponseEntity.ok("Reports deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
// Get the ID of the top-performing user
    @GetMapping("/topper")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportSummaryDTO> getTopper() {
        return reportService.returnTopper();
    }


    
    
    @GetMapping("/rank")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public int getRank(@RequestParam Integer userId){
    	return reportService.returnRank(userId);
    }
    
 // Delete all reports in the system
    @DeleteMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllReports() {
        reportService.deleteAllReports();
        return ResponseEntity.ok("All reports have been deleted successfully.");
    }

    
    

    
    /*
    @GetMapping("/export")
    public ResponseEntity<String> exportReports() {
        String csv = reportService.exportReportsAsCSV();
        return ResponseEntity.ok(csv);
    }*/
    /*
    @GetMapping("/export")
    public ResponseEntity<?> exportReportsToFile() {
        String result = reportService.exportReportsToFile();
        if (result.equals("No data in reports")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok("Reports exported successfully to: " + result);
    }
    */
    
    
 

}
