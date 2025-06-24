package com.onlineexam.report.controller;

import com.onlineexam.report.dto.ReportSummaryDTO;
import com.onlineexam.report.entity.Report;
import com.onlineexam.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/analytics/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Create a new report for a specific user and exam using query parameters
    // Removed @PreAuthorize as security is disabled for this service.
    // Removed @RequestHeader as this service no longer expects JWT from clients.
    @PostMapping
    public ResponseEntity<ReportSummaryDTO> createReport(@RequestParam Integer userId, @RequestParam Integer examId) {
        // No authorizationHeader is passed here, assuming Response Service also handles its own security
        // or a gateway handles it upstream.
        Report report = reportService.generateReport(userId, examId);

        ReportSummaryDTO dto = new ReportSummaryDTO(
                report.getReportId(),
                report.getExamId(),
                report.getUserId(),
                report.getTotalMarks(),
                report.getPerformanceMetrics()
        );

        return ResponseEntity.ok(dto);
    }

    // Get all reports in the system
    // Removed @PreAuthorize
    @GetMapping
    public ResponseEntity<List<ReportSummaryDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/exam/{examId}")
    // Removed @PreAuthorize
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
    // Removed @PreAuthorize
    @DeleteMapping
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
    // Removed @PreAuthorize
    @GetMapping("/topper")
    public ResponseEntity<ReportSummaryDTO> getTopper() {
        return reportService.returnTopper();
    }


    @GetMapping("/rank")
    // Removed @PreAuthorize
    public int getRank(@RequestParam Integer userId) {
        return reportService.returnRank(userId);
    }
    
    // Delete all reports in the system
    // Removed @PreAuthorize
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllReports() {
        reportService.deleteAllReports();
        return ResponseEntity.ok("All reports have been deleted successfully.");
    }
}
