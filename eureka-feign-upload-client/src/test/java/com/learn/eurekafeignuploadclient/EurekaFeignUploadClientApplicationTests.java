package com.learn.eurekafeignuploadclient;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RunWith(SpringJUnit4ClassRunner.class)  //要删除 spring-boot-starter-test的scope，不然不能用runwith
@SpringBootTest
class EurekaFeignUploadClientApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(EurekaFeignUploadClientApplicationTests.class);
    @Autowired
    private UploadService uploadService;

    @Test
    public  void  testHandleFileUpload(){

        File file = new File("upload-test.txt");
        DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem("file",
                MediaType.TEXT_PLAIN_VALUE, true, file.getName());
        try(
                InputStream inputStream = new FileInputStream(file);
                OutputStream outputStream = fileItem.getOutputStream();
        ){
            IOUtils.copy(inputStream, outputStream);
        }catch (Exception e){
            throw  new IllegalArgumentException("Invalid file "+ e, e);
        }

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        logger.info("模拟上传开始");
        logger.info(uploadService.handleFileUpload(multipartFile));
        logger.info("模拟上传结束");
    }
    @Test
    void contextLoads() {
    }

}
