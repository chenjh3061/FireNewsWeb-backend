package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
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
    @PostMapping("/upload")
    public BaseResponse<?> upload(MultipartFile file) {
        if (file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "file is empty");
        } else if (file.getSize() > 4096 * 4096) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "file size is too large");
        }

        String originalFilename = file.getOriginalFilename();
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        assert originalFilename != null;
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
        return ResultUtils.error(ErrorCode.OPERATION_ERROR,"图片上传失败");
    }
}


