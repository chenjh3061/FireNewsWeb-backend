package com.example.firenewsbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ErrorCode;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.model.entity.Log;
import com.example.firenewsbackend.model.vo.DashboardVO;
import com.example.firenewsbackend.service.AdminService;
import com.example.firenewsbackend.service.LogService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
class FileInfo {
    private long id;
    @Setter
    private String name;
    @Setter
    private String fileType;
    @Setter
    private String url;

    public FileInfo() {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

}
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private LogService  logService;

    @Resource
    private AdminService adminService;

    @Value("${file.upload.dir:src/main/resources/static/uploads}")
    private String uploadDir;

    @GetMapping("/getDashboardData")
    public BaseResponse<DashboardVO> DashboardData(){
        StpUtil.checkRole("admin");
        DashboardVO dashboardVO = new DashboardVO();
        dashboardVO.setUserNum(adminService.getUserNum());
        dashboardVO.setArticleNum(adminService.getArticleNum());
        dashboardVO.setTodayViewCount(12345L);
        dashboardVO.setRunningDays(102L);
        return ResultUtils.success(dashboardVO);
    }

    @GetMapping("/getRecentLog")
    public BaseResponse<List<Log>> getRecentLog(){
        StpUtil.checkRole("admin");
        int limit = 10;
        return ResultUtils.success(logService.getRecentLog(limit));
    }

    // 获取文件列表
    @GetMapping("/files")
    public BaseResponse<List<FileInfo>> getFiles(@RequestParam(required = false) String type, @RequestParam(required = false) String query) {
        StpUtil.checkRole("admin");
        List<FileInfo> fileList = new ArrayList<>();
        File uploadDirectory = new File(uploadDir);
        if (uploadDirectory.exists() && uploadDirectory.isDirectory()) {
            File[] files = uploadDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setName(file.getName());
                        fileInfo.setFileType(getFileType(file.getName()));
                        fileInfo.setUrl("http://localhost:8089/uploads/" + file.getName());

                        // 过滤文件类型和搜索关键字
                        if ((type == null || type.equals("所有") || type.equalsIgnoreCase(fileInfo.getFileType())) &&
                                (query == null || fileInfo.getName().contains(query))) {
                            fileList.add(fileInfo);
                        }
                    }
                }
            }
        }
        return ResultUtils.success(fileList);
    }

    // 删除文件
    @DeleteMapping("/files/{id}")
    public BaseResponse<String> deleteFile(@PathVariable Long id) {
        StpUtil.checkRole("admin");
        BaseResponse<List<FileInfo>> fileResponse = getFiles(null, null);
        List<FileInfo> fileList = fileResponse.getData();
        for (FileInfo fileInfo : fileList) {
            if (fileInfo.getId() == id) {
                String filePath = uploadDir + File.separator + fileInfo.getName();
                Path path = Paths.get(filePath);
                try {
                    if (Files.deleteIfExists(path)) {
                        return ResultUtils.success("文件删除成功");
                    } else {
                        return (BaseResponse<String>) ResultUtils.error(ErrorCode.OPERATION_ERROR, "文件删除失败");
                    }
                } catch (Exception e) {
                    return (BaseResponse<String>) ResultUtils.error(ErrorCode.OPERATION_ERROR, "操作出错: " + e.getMessage());
                }
            }
        }
        return (BaseResponse<String>) ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "未找到该文件");
    }

    // 获取文件类型
    private String getFileType(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            String extension = fileName.substring(lastIndex + 1).toLowerCase();
            switch (extension) {
                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                    return "image";
                case "mp4":
                case "avi":
                case "mov":
                    return "video";
                case "mp3":
                case "wav":
                case "ogg":
                    return "audio";
                case "pdf":
                case "doc":
                case "docx":
                case "txt":
                    return "document";
                default:
                    return "others";
            }
        }
        return "others";
    }
}
