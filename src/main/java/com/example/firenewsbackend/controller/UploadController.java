package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.aop.LoggableOperation;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.common.ResultUtils;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.swing.text.Document;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/upload")
public class UploadController {

    //    @RequestMapping("/uploads/{filename:.+}")
    //    @ResponseBody
    //    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    //        // 假设上传文件的路径是 uploads 文件夹
    //        String uploadDirectory = "/path/to/your/uploaded/files"; // 设置正确的路径
    //        Path filePath = Paths.get(uploadDirectory).resolve(filename).normalize();
    //
    //        // 创建文件资源
    //        File file = filePath.toFile();
    //        if (!file.exists()) {
    //            return ResponseEntity.notFound().build();
    //        }
    //
    //        Resource resource = new FileSystemResource(file);
    //
    //        // 返回文件资源，不设置缓存策略（可以后续加上缓存逻辑）
    //        return ResponseEntity.ok().body(resource);
    //    }

    @Operation(summary = "上传图片到本地")
    @LoggableOperation(operationName = "上传图片", actionType = "add", targetType = "img")
    @PostMapping("/img")
    public BaseResponse<?> upload(MultipartFile file) {
        StpUtil.checkLogin();
        System.out.println("文件情况：" + file);
        if (file == null) {

            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "file is empty");
        } else if (file.getSize() > 4096 * 4096) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "file size is too large");
        }

        String originalFilename = file.getOriginalFilename();
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        assert originalFilename!= null;
        String fileNameSuffix = "." + originalFilename.split("\\.")[1];
        String fileName = fileNamePrefix + fileNameSuffix;

        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        // 存储路径
        String pre = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath() + "\\fireNews-Backend\\src\\main\\resources\\static\\uploads\\";
        System.out.println(pre);
        File dir = new File(pre);
        if (!dir.exists()) {
            dir.mkdirs();  // 如果目录不存在，创建目录
        }

        String path = pre + fileName;

        try {
            file.transferTo(new File(path));

            // 返回相对路径，前端可以通过这个路径访问文件
            return ResultUtils.success("/uploads/" + fileName);   // 例如：/uploads/20250107225712547.png
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR, "图片上传失败");
    }

    @Operation(summary = "下载图片")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("filePath") String filePath) {
        StpUtil.checkLogin();
        try {
            // 定义文件存储的根路径
            ApplicationHome applicationHome = new ApplicationHome(this.getClass());
            String pre = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath() + "\\fireNews-Backend\\src\\main\\resources\\static\\uploads\\";
            File file = new File(pre + filePath); // 拼接实际路径

            // 判断文件是否存在
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 文件不存在返回404
            }

            // 创建Resource对象
            Resource resource = new FileSystemResource(file);
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 文件不存在返回404
            }

            // 返回文件并设置响应头为下载类型
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 服务器错误
        }
    }

    @Operation(summary = "上传文档文件并解析为HTML")
    @LoggableOperation(operationName = "上传文件", actionType = "add", targetType = "doc")
    @PostMapping("/document")
    public BaseResponse<?> uploadAndParse(@RequestParam("file") MultipartFile file) {
//        StpUtil.checkLogin();
        if (file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件为空");
        }

        if (file.getSize() > 4096 * 4096 * 10) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件过大");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||!(originalFilename.endsWith(".doc") || originalFilename.endsWith(".docx") ||
                originalFilename.endsWith(".txt") || originalFilename.endsWith(".md"))) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "不支持的文件类型");
        }

        try {
            String htmlContent = parseFile(file, originalFilename);

            if (htmlContent == null) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "文件解析失败");
            }

            // 返回HTML内容，前端可跳转到文章编辑页面并填充
            System.out.println(htmlContent);
            return ResultUtils.success(htmlContent);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "文件处理失败");
        }
    }

    // 通用解析方法
    private String parseFile(MultipartFile file, String filename) throws IOException {
        if (filename.endsWith(".doc")) {
            return parseDoc(file);
        } else if (filename.endsWith(".docx")) {
            return parseDocx(file);
        } else if (filename.endsWith(".txt")) {
            return parseTxt(file);
        } else if (filename.endsWith(".md")) {
            return parseMarkdown(file);
        }
        return null;
    }

    // 解析.doc 文件
    private String parseDoc(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             HWPFDocument doc = new HWPFDocument(inputStream)) {

            Range range = doc.getRange();
            StringBuilder html = new StringBuilder();
            html.append("<div class=\"doc-content\">");

            StyleSheet styleSheet = doc.getStyleSheet();
            PicturesTable picturesTable = doc.getPicturesTable();

            // 记录图片的位置（段落索引, 字符运行索引）和文件名
            Map<String, String> positionToFileName = new HashMap<>();

            // 第一次遍历：提取所有图片并保存
            for (int paraIdx = 0; paraIdx < range.numParagraphs(); paraIdx++) {
                Paragraph paragraph = range.getParagraph(paraIdx);
                for (int runIdx = 0; runIdx < paragraph.numCharacterRuns(); runIdx++) {
                    CharacterRun run = paragraph.getCharacterRun(runIdx);
                    if (isPictureRun(run, picturesTable)) {
                        Picture picture = extractPicture(run, picturesTable);
                        if (picture != null) {
                            String imageFileName = savePicture(picture.getContent(), picture.suggestFullFileName());
                            if (imageFileName != null) {
                                // 用段落索引和字符运行索引作为唯一键
                                String positionKey = paraIdx + "-" + runIdx;
                                positionToFileName.put(positionKey, imageFileName);
                            }
                        }
                    }
                }
            }

            // 第二次遍历：生成 HTML 并插入图片
            for (int paraIdx = 0; paraIdx < range.numParagraphs(); paraIdx++) {
                Paragraph paragraph = range.getParagraph(paraIdx);
                StringBuilder paraHtml = new StringBuilder();

                // 处理段落样式（标题或普通段落）
                boolean isHeading = false;
                int headingLevel = 0;
                int styleIndex = paragraph.getStyleIndex();
                StyleDescription styleDesc = styleSheet.getStyleDescription(styleIndex);
                if (styleDesc != null && styleDesc.getName() != null && styleDesc.getName().contains("Heading")) {
                    try {
                        headingLevel = Integer.parseInt(styleDesc.getName().replace("Heading", ""));
                        isHeading = true;
                    } catch (NumberFormatException e) {
                        // 非标题样式
                    }
                }

                // 开始段落标签
                if (isHeading) {
                    paraHtml.append(String.format("<h%d>", headingLevel));
                } else {
                    paraHtml.append("<p>");
                }

                // 遍历字符运行，处理文本和图片
                for (int runIdx = 0; runIdx < paragraph.numCharacterRuns(); runIdx++) {
                    CharacterRun run = paragraph.getCharacterRun(runIdx);
                    String positionKey = paraIdx + "-" + runIdx;

                    // 检查是否有图片需要插入
                    if (positionToFileName.containsKey(positionKey)) {
                        String imageFileName = positionToFileName.get(positionKey);
                        paraHtml.append(String.format(
                                "<img src=\"http://localhost:8089/uploads/%s\" alt=\"image\">",
                                imageFileName
                        ));
                    } else {
                        // 处理文本格式（例如加粗）
                        String text = run.text();
                        if (run.isBold()) {
                            paraHtml.append("<strong>").append(text).append("</strong>");
                        } else {
                            paraHtml.append(text);
                        }
                    }
                }

                // 闭合段落标签
                if (isHeading) {
                    paraHtml.append(String.format("</h%d>", headingLevel));
                } else {
                    paraHtml.append("</p>");
                }

                html.append(paraHtml);
            }

            html.append("</div>");
            return html.toString();
        }
    }

    private boolean isPictureRun(CharacterRun run, PicturesTable picturesTable) {
        // 这里可以根据实际情况进行更精确的判断
        // 例如检查 run 的某些属性是否符合图片特征
        return picturesTable.hasPicture(run);
    }

    private Picture extractPicture(CharacterRun run, PicturesTable picturesTable) {
        return picturesTable.extractPicture(run, false);
    }

    private String savePicture(byte[] pictureContent, String suggestedFileName) {
        try {
            // 获取应用程序所在目录
            ApplicationHome applicationHome = new ApplicationHome(this.getClass());
            String pre = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath() + "\\fireNews-Backend\\src\\main\\resources\\static\\uploads\\";
            File dir = new File(pre);
            if (!dir.exists()) {
                boolean mkdirsResult = dir.mkdirs();
                System.out.println("目录创建结果: " + mkdirsResult);
                if (!mkdirsResult) {
                    System.err.println("无法创建目录: " + pre);
                    return null;
                }
            }

            String fileExtension = suggestedFileName.substring(suggestedFileName.lastIndexOf('.') + 1);
            String imageFileName = UUID.randomUUID().toString() + "." + fileExtension;
            String imageFilePath = pre + imageFileName;

            try (FileOutputStream os = new FileOutputStream(imageFilePath)) {
                os.write(pictureContent);
            }
            return imageFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 解析.docx 文件
    // 解析.docx 文件
    private String parseDocx(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {

            StringBuilder html = new StringBuilder();
            html.append("<div class=\"docx-content\">");

            // 遍历所有段落
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String style = paragraph.getStyle();
                StringBuilder textBuilder = new StringBuilder();
                for (XWPFRun run : paragraph.getRuns()) {
                    StringBuilder styleBuilder = new StringBuilder();
                    if (run.isBold()) {
                        styleBuilder.append("font-weight:bold;");
                    }
                    if (run.getFontSize() != -1) {
                        styleBuilder.append("font-size:").append(run.getFontSize()).append("pt;");
                    }
                    // 这里可以添加更多格式处理，如斜体、下划线等
                    if (styleBuilder.length() > 0) {
                        textBuilder.append("<span style=\"").append(styleBuilder).append("\">").append(run.getText(0)).append("</span>");
                    } else {
                        textBuilder.append(run.getText(0));
                    }
                }
                String text = textBuilder.toString();

                // 根据样式识别标题
                if (style != null && style.startsWith("Heading")) {
                    int level = Integer.parseInt(style.replace("Heading", ""));
                    html.append(String.format("<h%d>%s</h%d>", level, text, level));
                } else {
                    // 普通段落
                    html.append("<p>").append(text).append("</p>");
                }
            }

            // 处理表格
            for (XWPFTable table : document.getTables()) {
                html.append("<table border=\"1\">");
                for (XWPFTableRow row : table.getRows()) {
                    html.append("<tr>");
                    for (XWPFTableCell cell : row.getTableCells()) {
                        html.append("<td>").append(cell.getText()).append("</td>");
                    }
                    html.append("</tr>");
                }
                html.append("</table>");
            }

            // 处理图片
            for (XWPFPictureData pictureData : document.getAllPictures()) {
                String imageFileName = savePicture(pictureData);
                if (imageFileName != null) {
                    html.append("<img src=\"http://localhost:8089/uploads/").append(imageFileName).append("\" alt=\"image\">");
                }
            }

            html.append("</div>");
            return html.toString();
        }
    }

    private String savePicture(XWPFPictureData pictureData) {
        try {
            // 获取应用程序所在目录
            ApplicationHome applicationHome = new ApplicationHome(this.getClass());
            String pre = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath() + "\\fireNews-Backend\\src\\main\\resources\\static\\uploads\\";
            File dir = new File(pre);
            if (!dir.exists()) {
                boolean mkdirsResult = dir.mkdirs();
                System.out.println("目录创建结果: " + mkdirsResult);
                if (!mkdirsResult) {
                    System.err.println("无法创建目录: " + pre);
                    return null;
                }
            }

            String fileExtension = pictureData.suggestFileExtension();
            String imageFileName = UUID.randomUUID().toString() + "." + fileExtension;
            String imageFilePath = pre + imageFileName;

            try (FileOutputStream os = new FileOutputStream(imageFilePath)) {
                os.write(pictureData.getData());
            }
            return imageFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 解析.txt 文件
    private String parseTxt(MultipartFile file) throws IOException {
        return readFileToHtml(file);
    }

    // 解析.md 文件
    private String parseMarkdown(MultipartFile file) throws IOException {
        String content = readFileToHtml(file);

        // 配置 Flexmark 扩展
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                TaskListExtension.create(),
                StrikethroughExtension.create()
        ));

        Parser parser = Parser.builder(options).build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        return renderer.render(document);
    }

    // 通用的文件读取方法
    private String readFileToHtml(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                content.append(line).append("\n");
            }
            return formatHtml(content.toString());
        }
    }

    // 格式化为HTML
    private String formatHtml(String content) {
        // 将换行符替换为<br>
        // 使用Jsoup进行HTML格式化
        //Jsoup.clean(, Safelist.relaxed());
        return content.replace("\n", "<br>");
    }
}