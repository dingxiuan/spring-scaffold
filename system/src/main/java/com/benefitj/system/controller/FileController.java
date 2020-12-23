package com.benefitj.system.controller;

import com.benefitj.core.file.IUserFileManager;
import com.benefitj.spring.BreakPointTransmissionHelper;
import com.benefitj.spring.aop.AopWebPointCut;
import com.benefitj.system.file.SystemFileManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 文件操作
 */
@AopWebPointCut
@Api(tags = {"文件操作"}, description = "上传/下载 文件")
@RestController
@RequestMapping("/files")
public class FileController {

  @Autowired
  private SystemFileManager systemFileManager;

  @GetMapping("/download")
  public void download(HttpServletRequest request,
                       HttpServletResponse response,
                       String type,
                       String filename) throws IOException {
    IUserFileManager fileManager = systemFileManager.currentUser();
    File file = fileManager.getFile(type, filename);
    if (!file.exists()) {
      response.encodeRedirectURL("/error/404.html");
      return;
    }
    // 文件下载
    BreakPointTransmissionHelper.download(request, response, file, filename);
  }

  @ApiOperation("上传文件")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "file", value = "文件", required = true),
      @ApiImplicitParam(name = "type", value = "文件类型"),
      @ApiImplicitParam(name = "filename", value = "文件名称"),
      @ApiImplicitParam(name = "deleteIfExist", value = "如果存在是否删除", defaultValue = "false"),
  })
  @GetMapping("/upload")
  public void upload(HttpServletRequest request,
                     MultipartFile file,
                     String type,
                     String filename,
                     Boolean deleteIfExist) throws IOException {
    IUserFileManager fileManager = systemFileManager.currentUser();
    File destFile = fileManager.getFile(type, filename);
    if (!destFile.exists()) {
      if (!Boolean.TRUE.equals(deleteIfExist)) {
        return;
      }
      fileManager.delete(destFile);
    }
    // 上传下载
    BreakPointTransmissionHelper.upload(request, file, destFile);
  }

}
