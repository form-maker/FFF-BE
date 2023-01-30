package com.formmaker.fff.stats.controller;


import com.formmaker.fff.common.response.ResponseMessage;
import com.formmaker.fff.common.response.security.UserDetailsImpl;
import com.formmaker.fff.stats.dto.StatsResponse;
import com.formmaker.fff.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey/stats")
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getStats(@RequestParam Long surveyId, @RequestParam String start, @RequestParam String end, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StatsResponse statsResponse = statsService.getStats(surveyId, start, end, userDetails.getUserId());

        ResponseMessage responseMessage = new ResponseMessage<>("통계 조회 성공", 200, statsResponse);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatusCode()));
    }


    @GetMapping("/download/csv")
    public ResponseEntity<byte[]> downloadCSV(@RequestParam Long surveyId) throws IOException{
        Pair<String, byte[]> csvFile = statsService.getStatsCsvFile(surveyId);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf("plain/text"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ URLEncoder.encode(csvFile.getFirst(), "utf-8") +".csv");
        header.setContentLength(csvFile.getSecond().length);
        return new ResponseEntity<>(csvFile.getSecond(), header, HttpStatus.OK);
    }

    @GetMapping("/download/xlsx")
    public void downloadXLSX(@RequestParam Long surveyId, HttpServletResponse response) throws IOException {
        XSSFWorkbook xssfWorkbook = statsService.getStatsXlsxFile(surveyId);
        HttpHeaders header = new HttpHeaders();
        response.setContentType("ms-vnd/excel");
        String fileName = xssfWorkbook.getSheetName(0);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8")+".xlsx");
        xssfWorkbook.write(response.getOutputStream());
        xssfWorkbook.close();
    }

}
