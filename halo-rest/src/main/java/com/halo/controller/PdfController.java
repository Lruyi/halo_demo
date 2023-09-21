package com.halo.controller;

import com.halo.common.Result;
import com.itextpdf.text.*;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/7/21 18:40
 */
@RestController
@RequestMapping ("/pdf")
public class PdfController {

    @GetMapping ("/generatePdfDemo")
    public Result<Object> generatePdfDemo() {
        // 准备模板数据
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Sample PDF");
        data.put("greeting", "Hello, World!");
        data.put("content", "This is a sample PDF generated using FreeMarker and Apache PDFBox.");
        try {
            // 加载 FreeMarker 模板
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setClassForTemplateLoading(PdfController.class, "/templates/");
            Template template = cfg.getTemplate("template_simple.ftl");

            //创建PDF文件
//            PDDocument document = new PDDocument();
//            PDPage page = new PDPage();
//            document.addPage(page);

            // 将模板内容写入 PDF 页面
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Writer writer = new OutputStreamWriter(outputStream);
            StringWriter stringWriter = new StringWriter();
            template.process(data, stringWriter);
            stringWriter.flush();

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // 将模板内容写入 PDF 页面
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.showText(stringWriter.toString());
            contentStream.endText();
            contentStream.close();

            // 保存 PDF 文件
            document.save("/Users/liuruyi/Downloads/template-simple.pdf");
            document.close();

            System.out.println("PDF created successfully.");
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @GetMapping ("/generatePdfDemo2")
    public Object generatePdfDemo2() {
        // 准备学生数据
        Map<String, Object> data = new HashMap<>();
        data.put("student", createStudentData());


        try {
            // 加载 FreeMarker 模板
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setClassForTemplateLoading(PdfController.class, "/templates/");
            Template template = cfg.getTemplate("report_template.ftl");

            // 创建 PDF 文件
            PDDocument document = new PDDocument();

            // 添加多个页面
            PDPage page = new PDPage();
            document.addPage(page);


            // 将数据填充到模板中
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);
            template.process(data, writer);
            writer.flush();
            // 将模板内容写入 PDF 页面
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(outputStream.toString().replace("\n", "<br>"));
//            contentStream.showText("Hello World");
            contentStream.endText();
            contentStream.close();

            System.out.println(outputStream);

            // 保存 PDF 文件
            document.save("/Users/liuruyi/Downloads/student_report.pdf");
            document.close();

            System.out.println("PDF created successfully.");
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 创建学生数据
    private static Map<String, Object> createStudentData() {
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("name", "John Doe");
        studentData.put("id", "12345");
        studentData.put("age", 20);
        studentData.put("address", "123 Main St, City");
        studentData.put("photo", "/Users/liuruyi/Downloads/景色.png");

        // 创建学生成绩表格
        List<Map<String, Object>> scores = new ArrayList<>();
        scores.add(createScoreData("Math", 90));
        scores.add(createScoreData("Science", 85));
        scores.add(createScoreData("History", 78));
        studentData.put("scores", scores);

        return studentData;
    }

    // 创建学生成绩数据
    private static Map<String, Object> createScoreData(String subject, int score) {
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("subject", subject);
        scoreData.put("score", score);
        return scoreData;
    }

    @GetMapping ("/generatePdfDemo3")
    public Object generatePdfDemo4iText3() {
        // 准备模板数据
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Sample PDF");
        data.put("greeting", "Hello, World!");
        data.put("content", "This is a sample PDF generated using FreeMarker and iText.");

        try {
            // 加载 FreeMarker 模板
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setClassForTemplateLoading(PdfController.class, "/templates/");
            Template template = cfg.getTemplate("template_simple.ftl");

            // 将数据填充到模板中
            StringWriter stringWriter = new StringWriter();
            template.process(data, stringWriter);
            String generatedContent = stringWriter.toString();

            // 创建 PDF 文件
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("template-simple.pdf"));

            document.open();

            // 将生成的内容添加到 PDF 中
            Paragraph paragraph = new Paragraph(generatedContent, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.RED));
//            Paragraph p = new Paragraph("This is a paragraph",
//                    FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.RED));
            document.add(paragraph);

            document.close();

            System.out.println("PDF created successfully.");
        } catch (IOException | TemplateException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping ("/generatePdfDemo4")
    public Object generatePdfDemo4iText4() {
        // 准备学生数据
        List<Map<String, Object>> studentDataList = createStudentDataList();

        try {
            // 加载 FreeMarker 模板
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setClassForTemplateLoading(PdfController.class, "/templates/");
            Template template = cfg.getTemplate("complex_template.ftl");

            // 创建 PDF 文件
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("complex_output.pdf"));

            document.open();

            // 将数据填充到模板中，并生成多个页面的 PDF
            for (Map<String, Object> studentData : studentDataList) {
                StringWriter stringWriter = new StringWriter();
                template.process(studentData, stringWriter);
                String generatedContent = stringWriter.toString();

                Paragraph paragraph = new Paragraph();
                paragraph.add(new Chunk(generatedContent));

                document.add(paragraph);
                document.newPage();
            }

            document.close();

            System.out.println("Complex PDF created successfully.");
        } catch (IOException | TemplateException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 可以生成PDF，简单的demo
     * @return
     */
    @GetMapping ("/generatePdfDemo5")
    public Object generatePdfDemo4iText5() {
        // 设置 FreeMarker 配置
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(PdfController.class, "/templates/");

        // 创建数据模型
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Sample PDF Export");
        data.put("content", "This is the content of the PDF.");

        try {
            // 加载模板
            Template template = cfg.getTemplate("template_simple_itext.ftl");

            // 创建 PDF 文档
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("template_simple_itext.pdf"));

            document.open();

            // 使用 FreeMarker 填充数据到模板并写入 PDF 文档
            StringWriter stringWriter = new StringWriter();
            template.process(data, stringWriter);

            HTMLWorker worker = new HTMLWorker(document);
            worker.parse(new StringReader(stringWriter.toString()));

            document.close();

            System.out.println("PDF generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 可以生成PDF，但是数据只支持map，不支持list
     * @return
     */
    @GetMapping ("/generatePdfDemo6")
    public Object generatePdfDemo4iText6() {
        // 设置 FreeMarker 配置
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(PdfController.class, "/templates/");

        // 创建数据模型
        Map<String, Object> studentData = createStudentData();

        try {
            // 加载模板
            Template template = cfg.getTemplate("complex_template111.ftl");

            // 创建 PDF 文档
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("complex_template-1111.pdf"));

            document.open();

            // 使用 FreeMarker 填充数据到模板并写入 PDF 文档
            StringWriter stringWriter = new StringWriter();
            template.process(studentData, stringWriter);

            HTMLWorker worker = new HTMLWorker(document);
            worker.parse(new StringReader(stringWriter.toString()));

            document.close();

            System.out.println("PDF generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping ("/generatePdfDemo7")
    public Object generatePdfDemo4iText7() {
        CompletableFuture.supplyAsync(() -> "");
        // 设置 FreeMarker 配置
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(PdfController.class, "/templates/");
        // 准备学生数据
        Map<String, Object> data = new HashMap<>();
        data.put("student", createStudentData());

        try {
            // 加载模板
            Template template = cfg.getTemplate("complex_template222.ftl");

            // 创建 PDF 文档
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("complex_template-222.pdf"));

            document.open();

            // 使用 FreeMarker 填充数据到模板并写入 PDF 文档
            StringWriter stringWriter = new StringWriter();
            template.process(data, stringWriter);

            HTMLWorker worker = new HTMLWorker(document);
            worker.parse(new StringReader(stringWriter.toString()));

            document.close();

            System.out.println("PDF generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // 创建学生数据列表
    private static List<Map<String, Object>> createStudentDataList() {
        List<Map<String, Object>> studentDataList = new ArrayList<>();

        studentDataList.add(createStudentData("John Doe", "12345", 20, "123 Main St, City", "path/to/photo1.jpg"));
        studentDataList.add(createStudentData("Jane Smith", "67890", 22, "456 Oak St, Town", "path/to/photo2.jpg"));

        return studentDataList;
    }

    // 创建单个学生数据
    private static Map<String, Object> createStudentData(String name, String id, int age, String address, String photo) {
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("name", name);
        studentData.put("id", id);
        studentData.put("age", age);
        studentData.put("address", address);
        studentData.put("photo", photo);

        // 创建学生成绩表格
        List<Map<String, Object>> scores = new ArrayList<>();
        scores.add(createScoreData1("Math", 90));
        scores.add(createScoreData1("Science", 85));
        scores.add(createScoreData1("History", 78));
        studentData.put("scores", scores);

        return studentData;
    }

    // 创建学生成绩数据
    private static Map<String, Object> createScoreData1(String subject, int score) {
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("subject", subject);
        scoreData.put("score", score);
        return scoreData;
    }
}
