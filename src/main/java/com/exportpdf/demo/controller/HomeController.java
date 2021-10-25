package com.exportpdf.demo.controller;

import com.exportpdf.demo.mapper.GoodsMapper;
import com.exportpdf.demo.mapper.ReportsMapper;
import com.exportpdf.demo.pojo.Goods;
import com.exportpdf.demo.pojo.Reports;
import com.exportpdf.demo.service.PdfTableService;
import com.exportpdf.demo.util.*;
import com.exportpdf.demo.util.ViewPdfUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private ReportsMapper reportsMapper;
    @Resource
    PdfTableService pdfTableService;


    //从浏览器直接显示pdf
    @GetMapping("/viewexistpdf")
    public void viewexistpdf() {
        String filePath = "/data/springboot2/测试报告.pdf";
        PdfUtil.readPdfFile(filePath);

    }

    //把数据保存到pdf文件
    @GetMapping("/savepdf")
    public String savepdf() {
        List<Goods> goodsList = goodsMapper.selectAllGoods();
        String savePath = "/data/springboot2/goodslist.pdf";
        pdfTableService.createPDF(new Document(PageSize.A4), goodsList,savePath);
        return "pdf saveed";
    }

    @GetMapping("/savepdf2")
    public String readPDF() {
        String savePath = "/data/springboot2/goodslist2.pdf";
        pdfTableService.createPDF(new Document(PageSize.A4),savePath);
        return "pdf saveed";
    }

    //从浏览器直接显示pdf
    @GetMapping("/viewpdf")
    public ModelAndView viewpdf() {
        List<Goods> goodsList = goodsMapper.selectAllGoods();
        Map<String, Object> model = new HashMap<>();
        model.put("sheet", goodsList);
        ViewPdfUtil viewPdf = new ViewPdfUtil();
        viewPdf.setFileName("测试.pdf");
        viewPdf.setPdfType("goods");
        return new ModelAndView(viewPdf, model);
    }

    //下载pdf文件
    @GetMapping("/downpdf")
    public void downpdf() {
          String filepath = "/data/springboot2/测试报告.pdf";
          PdfUtil.downPdfFile(filepath);
     }

    @RequestMapping("/downloadPDF/{id}")
    public void fillTemplate(@PathVariable Integer id) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        Reports report = reportsMapper.getReportById(id);
        Map<String, Object> inputMap = new HashMap<>();
        HashMap<String, String> dateMap = new HashMap<>();
        dateMap.put("reportNo",report.getTestNo());
        dateMap.put("productName",report.getProjectName());
        //生成报告的名字
        dateMap.put("billId","测试报告新");
        inputMap.put("dateMap", dateMap);
        PdfUtil.pdfExport(inputMap,response);
    }

    @GetMapping("/hello")
    public String hello() {
        return "pdf saveed";
    }
}