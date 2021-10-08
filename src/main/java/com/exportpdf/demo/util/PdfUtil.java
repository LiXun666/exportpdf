package com.exportpdf.demo.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

public class PdfUtil {
    public static final String tPath = System.getProperty("user.dir");

     //read  a exist pdf file,not download
     public static void readPdfFile(String filePath) {
         ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         HttpServletResponse response = servletRequestAttributes.getResponse();
         File f = new File(filePath);//读取f
         if (!f.exists()) {
             return;
         }
         try {
             BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
             byte[] bs = new byte[1024];
             int len = 0;
             response.reset();
             response.setContentType("application/pdf");//
             response.setHeader("Content-Disposition", "inline;filename="
                     + URLEncoder.encode(f.getName(), "UTF-8"));
             // 文件名应该编码成utf-8
             OutputStream out = response.getOutputStream();
             while ((len = br.read(bs)) > 0) {
                 out.write(bs, 0, len);
             }
             out.flush();
             out.close();
             br.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

    //download a pdf file
    public static void downPdfFile(String filePath) {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        File f = new File(filePath);
        if (!f.exists()) {
            return;
        }
        try {
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
            byte[] bs = new byte[1024];
            int len = 0;
            response.reset();
                // 纯下载方式
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(f.getName(), "UTF-8"));
            OutputStream out = response.getOutputStream();
            while ((len = br.read(bs)) > 0) {
                out.write(bs, 0, len);
            }
            out.flush();
            out.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pdfOut(Map<String, Object> inputMap){
        // 生成的新文件路径
        String path0 = "F:/ProhibitDelete";
        File f = new File(path0);

        if (!f.exists()) {
            f.mkdirs();
        }
        // 模板路径
        String templatePath = "F:\\data\\springboot2\\testPDF.pdf";
        // 创建文件夹
        String newPdfPath = "F:/ProhibitDelete/testMould.pdf";
        File file = new File(templatePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file1 = new File(newPdfPath);
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/simfang.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // 输出流
            out = new FileOutputStream(newPdfPath);
            // 读取pdf模板
            reader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            Map<String, String> datemap = (Map<String, String>) inputMap.get("dateMap");
            form.addSubstitutionFont(bf);
            for (String key : datemap.keySet()) {

                String value = datemap.get(key);
                form.setField(key, value);
            }
//            for (int i = 0; i < 2; i++) {
//                stamper.getUnderContent(i).setColorFill(BaseColor.BLACK);
//            }
            stamper.setFormFlattening(false);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException | DocumentException e) {
            System.out.println(e);
        }
    }

    public static void pdfExport(Map<String, Object> inputMap, HttpServletResponse response) {

        // 生成的新文件路径
        String path0 = "D:/ProhibitDelete";
        File f = new File(path0);

        if (!f.exists()) {
            f.mkdirs();
        }
        // 模板路径
//        String templatePath = "F:\\data\\springboot2\\testPDF.pdf";
        String templatePath = "/data/测试报告.pdf";


        File file = new File(tPath+templatePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PdfReader reader; //读取模板
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        OutputStream out = null; //输出流
        try {
            Map<String, String> datemap = (Map<String, String>) inputMap.get("dateMap");
            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/simfang.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font bf1 = new Font(bf, 15,Font.BOLD);
            // 输出流
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(datemap.get("billId") + ".pdf", "UTF-8"));
            out = new BufferedOutputStream(response.getOutputStream()); //输出流
            // 读取pdf模板
            reader = new PdfReader(tPath+templatePath);  //读取模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            form.addSubstitutionFont(bf);
            for (String key : datemap.keySet()) {
                String value = datemap.get(key);
//                form.setFieldProperty(key,"textsize",18f,null);
                form.setField(key, value);
            }
            stamper.setFormFlattening(true);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();

//            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            //输出多张
            PdfImportedPage importPage = null;
            for (int i=1;i<=reader.getNumberOfPages();i++){
                importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
                copy.addPage(importPage);
            }
            copy.addPage(importPage);
            doc.close();

        } catch (IOException | DocumentException e) {
            System.out.println(e);
        } finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
