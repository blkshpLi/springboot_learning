package com.learning.springboot.controller;

import com.learning.springboot.dto.FileDTO;
import com.learning.springboot.provider.OSSProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    private OSSProvider ossProvider;

    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO upload(HttpServletRequest request) {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String generatedName = null;
        try {
            generatedName = ossProvider.upload(file.getInputStream(), file.getOriginalFilename());
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

        FileDTO fileDTO = new FileDTO();
        if(StringUtils.isBlank(generatedName)){
            fileDTO.setSuccess(0);
        }else {
            fileDTO.setSuccess(1);
            fileDTO.setUrl(ossProvider.getUrl(generatedName));
        }
        return fileDTO;
    }

}
