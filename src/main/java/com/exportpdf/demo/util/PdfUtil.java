package com.exportpdf.demo.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

import static com.exportpdf.demo.service.impl.PdfTableServiceImpl.getParagraphText;


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

    public static PdfPTable createTable2() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(4);//生成一个4列的表格
        int widths[] = { 10,40,40,10 };//指定各列的宽度百分比
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(widths);

        PdfPCell cell;
        int size = 20;

        Font font = new Font(BaseFont.createFont(new ClassPathResource("/font/FZLTHK.TTF").getPath(),BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
        font.setColor(BaseColor.BLACK);

        Font font_head = new Font(BaseFont.createFont(new ClassPathResource("/font/FZLTHK.TTF").getPath(),BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
        font_head.setColor(BaseColor.BLUE);

        Font font_title = new Font(BaseFont.createFont(new ClassPathResource("/font/FZLTHK.TTF").getPath(),BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED));
        font_title.setColor(BaseColor.GREEN);
        font_title.setSize(36);


        cell = new PdfPCell(new Phrase("商品库存信息表", font_title));
        cell.setColspan(4);//设置所占列数
        cell.setFixedHeight(50);//设置高度
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("ID",font_head));//商品编号
        cell.setFixedHeight(size);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("商品名称",font_head));//商品名称
        cell.setFixedHeight(size);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("描述",font_head));//描述
        cell.setFixedHeight(size);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("价格",font_head));//商品价格
        cell.setFixedHeight(size);
        table.addCell(cell);


        return table;
    }

    /**
     *
     * @param newPath 输入地址
     * @param finalPath 输出地址
     * @throws Exception 测试
     */
    public static void generateFinalPdf(String newPath, String finalPath, String baselineType) throws Exception{
        FileOutputStream outputStream = new FileOutputStream(finalPath);
        PdfReader reader = new PdfReader(newPath);// 读取pdf模板
        Rectangle pageSize = reader.getPageSize(1);
        Document document = new Document(pageSize);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cbUnder = writer.getDirectContentUnder();
        PdfImportedPage pageTemplate = writer.getImportedPage(reader, 1);
        cbUnder.addTemplate(pageTemplate, 0, 0);
        document.newPage();//新创建一页来存放后面生成的表格
        if ("create".equals(baselineType)) {
//            Paragraph paragraph = generatePdfATATable();//此处为生成的表格及内容方法，只已ＡＴＡ表为例，其余两个就不写了
            Paragraph para = getParagraphText("整个白酒行业从2012年开始，都迅速下滑，销量和利润都是大跌。2014年和2015年，茅台的股价涨得不错，但也没有超过同期的白马股太多，加上利润增速一直没有恢复塑化剂之前的状态，我就一直没有再买入");

            document.add(para);
        }
        document.close();
        reader.close();

    }

    private static Paragraph generatePdfATATable() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese, 10.5F, Font.NORMAL);// 五号
        Paragraph ret = new Paragraph("附表1： 基线按ATA章节分类情况统计表", fontChinese);
        PdfPTable tableBox = new PdfPTable(3);
        tableBox.setWidths(new float[]{0.3f, 0.4f, 0.3f});// 每个单元格占多宽
        tableBox.setWidthPercentage(80f);

        tableBox.addCell(getCell(new Phrase("基线按ATA章节分类情况", fontChinese), false, 3, 1));
        tableBox.addCell(getCell(new Phrase("ATA", fontChinese), false, 1, 1));
        tableBox.addCell(getCell(new Phrase("文件/图样数量", fontChinese), false, 1, 1));
        tableBox.addCell(getCell(new Phrase("DM数量", fontChinese), false, 1, 1));
        ret.add(tableBox);
        return ret;
    }

    //每个ｃｅｌｌ的格式，合并单元格情况
    private static PdfPCell getCell(Phrase phrase, boolean yellowFlag, int colSpan, int rowSpan) {
        PdfPCell cells = new PdfPCell(phrase);
        cells.setUseAscender(true);
        cells.setMinimumHeight(20f);
        cells.setHorizontalAlignment(1);
        cells.setVerticalAlignment(5);
        cells.setColspan(colSpan);
        cells.setRowspan(rowSpan);
        cells.setNoWrap(false);
        return cells;
    }
}
