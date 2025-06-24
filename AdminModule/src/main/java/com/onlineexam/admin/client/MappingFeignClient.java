package com.onlineexam.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mapping-service", url = "http://localhost:8084")
public interface MappingFeignClient {

	 // Replace with actual URL or use service discovery
	    @DeleteMapping("/api/exam-management/mappings/exam/{examId}")
	    void deleteMappingsByExamId(@PathVariable("examId") Integer examId);
	

}
