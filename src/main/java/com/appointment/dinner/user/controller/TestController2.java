package com.appointment.dinner.user.controller;

import com.appointment.dinner.message.R;
import com.appointment.dinner.user.model.SysUser;
import com.appointment.dinner.user.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class TestController2 {
    private static Logger logger = LoggerFactory.getLogger(TestController2.class);
    @Autowired
    private TestService service;



    //表单需要username、name、password、email
    @PostMapping(value = "/register")
    public R<String> register(SysUser sysUser, MultipartFile file) {
        //先保存个人图片，再保存个人信息
        try {
            if (file.isEmpty()) {
                logger.info("文件为空！");
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(new Date());
            String path = "/var/uploaded_files/" + date + "/";
            UUID uuid = UUID.randomUUID();
            String originalFilename = file.getOriginalFilename();//------API
            String extendName = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            String fileName = uuid.toString() + extendName;
            //创建一个文件
            File dir = new File(path, fileName);
            //创建文件路径
            File filepath = new File(path);
            if (!filepath.exists()) {
                filepath.mkdirs();
                System.out.println();//无用
            }

            file.transferTo(dir);//MultipartFile的内置方法transferTo()
            //把图片本身保存到服务器，然后写代码把图片URL保存到对应的数据库表中

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new R<>(e);
        }

        int insert_number = service.addUser(sysUser);
        return new R<String>("文件+图片上传成功");
    }




}
