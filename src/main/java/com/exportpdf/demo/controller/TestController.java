package com.exportpdf.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.exportpdf.demo.util.*;
/**
 * @Author lxjj
 * @Date 2021-10-22 14:57
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/generatePdf")
    public void generatePdf() throws Exception {
        String newPdf = "F:\\找工作\\studyspaces\\exportpdf\\data\\测试报告.pdf";
        String finalPath = "F:\\找工作\\studyspaces\\exportpdf\\data\\finalPath.pdf";
        PdfUtil.generateFinalPdf(newPdf,finalPath,"create");
        PdfUtil.downPdfFile(finalPath);
    }
}
