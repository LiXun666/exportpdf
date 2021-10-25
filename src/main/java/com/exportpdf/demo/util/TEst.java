//package com.exportpdf.demo.util;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.*;
//import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
//
//import java.io.FileOutputStream;
//import java.util.HashMap;
//import java.util.Iterator;
//
///**
// * @Author lxjj
// * @Date 2021-10-20 22:24
// * @Description
// */
//public class TEst {
//    public static void downloadCreateListPdf(BaselineBO baselineData) throws Exception {
////模板内所需要的数据（测试数据）
//
//        HashMap<String, String> map = new HashMap<String, String>();
//
//        map.put("0", "0");
//        map.put("1", "1");
//        map.put("2", "2");
//
//        map.put("3", "3");
//
//        map.put("4", "4");
//        map.put("5", "5");
//
//        map.put("6", "6");
//
//        map.put("7", "7");
//
//        map.put("8", "8");
//        map.put("9", "9");
//
//        map.put("10", "10");
//
//        map.put("11", "11");
//
//        String ret = "D：\\file\\baseline.pdf";//pdf模板
//        String newPath = "D：\\file\\BaselineContent.pdf";
//        // 1.读取pdf模板并写入数据
//        writePdfModel(ret, newPath, templateMap);
//        String finalPath = "D：\\file\\baselineCreate.pdf";//最终模板生成路径
//        String type = "create";
//        generateFinalPdf(newPath, finalPath, type, baselineData);
//    }
//
////向模板中写入数据
//    private static void writePdfModel(String inPath, String outPath, HashMap<String, String> templateMap)
//            throws Exception {
//        FileOutputStream outputStream = new FileOutputStream(outPath);
//        PdfReader reader1 = new PdfReader(inPath);// 读取pdf模板
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        PdfStamper stamper = new PdfStamper(reader1, bos);
//        AcroFields form = stamper.getAcroFields();//获取form域
//        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//        form.addSubstitutionFont(bfChinese);
//        Iterator<String> iterator = form.getFields().keySet().iterator();
//        int i = 0;
//        while (iterator.hasNext()) {
//            String name = iterator.next();
//            String key = String.valueOf(i);//form域我设置名称为0-11,方便循环set值
//            form.setField(name, templateMap.get(key));
//            i++;
//        }
//        stamper.setFormFlattening(true);
//        stamper.close();
//        outputStream.write(bos.toByteArray());
//        outputStream.flush();
//        outputStream.close();
//        bos.close();
//
//    }
//
//    /**
//     * 生成最终版本的pdf
//     *
//     * @param newPath       已写入数据的pdf模板路径
//     * @param finalPath     最终版本的pdf生成路径
//     * @param
//     * @throws Exception
//     */
//    private static void generateFinalPdf(String newPath, String finalPath, String baselineType, BaselineBO baselineData) throws Exception {
//        FileOutputStream outputStream = new FileOutputStream(finalPath);
//        PdfReader reader = new PdfReader(newPath);// 读取pdf模板
//        Rectangle pageSize = reader.getPageSize(1);
//        Document document = new Document(pageSize);
//        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
//        document.open();
//        PdfContentByte cbUnder = writer.getDirectContentUnder();
//        PdfImportedPage pageTemplate = writer.getImportedPage(reader, 1);
//        cbUnder.addTemplate(pageTemplate, 0, 0);
//        document.newPage();//新创建一页来存放后面生成的表格
//        if ("create".equals(baselineType)) {
//            Paragraph paragraph = generatePdfATATable(baselineData);//此处为生成的表格及内容方法，只已ＡＴＡ表为例，其余两个就不写了
//// ／／Paragraph paragraphFile = generatePdfFileTable(baselineData);
//// ／／Paragraph paragraphDM = generatePdfDMTable(baselineData);
//            document.add(paragraph);
//// ／／document.add(paragraphFile);
//// ／／document.add(paragraphDM);
//        }
//        document.close();
//        reader.close();
//
//    }
//
//    /**
//     * 生成pdf表格
//     *
//     * @return
//     * @see
//     */
//    private static Paragraph generatePdfATATable(BaselineBO baselineData) throws Exception {
//        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//        Font fontChinese = new Font(bfChinese, 10.5F, Font.NORMAL);// 五号
//        Paragraph ret = new Paragraph("附表1： 基线按ATA章节分类情况统计表", fontChinese);
//        PdfPTable tableBox = new PdfPTable(3);
//        tableBox.setWidths(new float[]{0.3f, 0.4f, 0.3f});// 每个单元格占多宽
//        tableBox.setWidthPercentage(80f);
//        // 获取ATA分类的结果集
//        List<BaselineATA> ataList = countFileOrDMByATA(baselineData);
//        // 创建表格格式及内容
//        tableBox.addCell(getCell(new Phrase("基线按ATA章节分类情况", fontChinese), false, 3, 1));
//        tableBox.addCell(getCell(new Phrase("ATA", fontChinese), false, 1, 1));
//        tableBox.addCell(getCell(new Phrase("文件/图样数量", fontChinese), false, 1, 1));
//        tableBox.addCell(getCell(new Phrase("DM数量", fontChinese), false, 1, 1));
//        // 遍历查询出的结果
//        for (BaselineATA ata : ataList) {
//            tableBox.addCell(getCell(new Phrase(ata.getAta(), fontChinese), false, 1, 1));
//            tableBox.addCell(getCell(new Phrase(String.valueOf(ata.getFileNumber()), fontChinese), false, 1, 1));
//            tableBox.addCell(getCell(new Phrase(String.valueOf(ata.getDMNumber()), fontChinese), false, 1, 1));
//        }
//        ret.add(tableBox);
//        return ret;
//
//    }
//
//    //每个ｃｅｌｌ的格式，合并单元格情况
//    private static PdfPCell getCell(Phrase phrase, boolean yellowFlag, int colSpan, int rowSpan) {
//        PdfPCell cells = new PdfPCell(phrase);
//        cells.setUseAscender(true);
//        cells.setMinimumHeight(20f);
//        cells.setHorizontalAlignment(1);
//        cells.setVerticalAlignment(5);
//        cells.setColspan(colSpan);
//        cells.setRowspan(rowSpan);
//        cells.setNoWrap(false);
//        return cells;
//    }
//}
