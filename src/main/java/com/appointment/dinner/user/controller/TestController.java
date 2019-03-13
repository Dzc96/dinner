package com.appointment.dinner.user.controller;

import com.appointment.dinner.exception.LogicalVerificationException;
import com.appointment.dinner.handle.GlobalExceptionHandler;
import com.appointment.dinner.message.R;
import com.appointment.dinner.user.model.SysUser;
import com.appointment.dinner.user.service.TestService;
import com.appointment.dinner.util.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Tony
 * @Time 2019/1/15
 */
@RestController
@RequestMapping("/user")
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     *  /user/{userId}   HTTP1.1 8 get  post  delete  put
     */
    @Autowired
    private TestService testService;

    @GetMapping("/v1/{userId}")
    public R<SysUser> getUserById(@PathVariable("userId")Long userId){
        return new R<>(testService.getUserById(userId));
    }



    @GetMapping("/v1/list")
    public R<List<SysUser>> list(){
        return new R<>(testService.getUserList());
    }

    @GetMapping("/v1/pages")
    public R<PageVO<SysUser>> pages(@RequestParam(value = "offset",defaultValue = "1")int offset,
                                    @RequestParam(value = "limit",defaultValue = "10")int limit){
        return new R<>(new PageVO<>(testService.getUserPages(offset,limit)));
    }

    @PutMapping("/v1")
    public R<String> updateUser(@Valid @RequestBody SysUser sysUser){
        int i = testService.updateUser(sysUser);
        if(i>0){
            return new R<>("更新成功");
        }else{
            return new R<>(new LogicalVerificationException("更新失败"));
        }
    }

    @DeleteMapping("/v1/{userId}")
    public R<String> deleteUser(@PathVariable("userId")Long userId){
        int i = testService.deleteUserById(userId);
        if(i>0){
            return new R<>("删除成功");
        }else{
            return new R<>(new LogicalVerificationException("删除失败"));
        }
    }

    /**
     * 这是处理非json参数的方法，这里的参数属于http的body部分，即请求报文的请求体，不是url后面的参数
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/app/verifyMsgCode1111")
    public R<String> login111(@RequestParam String username, @RequestParam String password) {
//        SysUser user = testService.getUserById(sysUser.getUserId());
        String username1 = username;
        String password1 = password;
        System.out.println("这是后台收到的username:" + username1);
        System.out.println("这是后台收到的password:" + password1);
        SysUser sysUser = new SysUser();
        sysUser.setName(username);
        sysUser.setPassword(password);
        SysUser user =  testService.getUserByUser(sysUser);
        if (user != null) {
            return new R<>("登陆成功");
        } else {
            return new R<>(new LogicalVerificationException("登陆失败"));
        }
    }


    /**
     * 这是处理json参数的方法，这里的参数依然是http请求报文的请求体部分body
     * @param sysUser
     * @return
     */
    @PostMapping("/app/verifyMsgCode222")
    public R<String> login222(@RequestBody SysUser sysUser) {
        SysUser user =  testService.getUserByUser(sysUser);
        System.out.println("--------------------");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println("--------------------");
        if (user != null) {
            return new R<>("登陆成功");
        } else {
            return new R<>(new LogicalVerificationException("登陆失败"));
        }
    }



    @PostMapping("/app/verifyMsgCode")
    public R<String> login(SysUser sysUser) {
        SysUser user =  testService.getUserByUser(sysUser);
        System.out.println("--------------------");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println("----");
        System.out.println("--------------------");
        if (user != null) {
            return new R<>("登陆成功");
        } else {
            return new R<>(new LogicalVerificationException("登陆失败"));
        }
    }


    @PostMapping("/app/verifyMsgCheck")
    public R<String> verifyMsgCheck (@RequestParam String phoneNumber, @RequestParam String verification) {
        if (phoneNumber != null & verification != null) {
            return new R<>("短信验证成功");
        }
        return new R<>("短信验证失败");
    }

    /**
     * 把MultipartFile转成File进行保存
     * @param image
     * @param request
     * @return
     */
    @PostMapping("/app/img/upload111")
    public R<String> picture111 (@RequestParam MultipartFile image, HttpServletRequest request) {
        if (image.isEmpty() || StringUtils.isBlank(image.getOriginalFilename())) {
            return new R<>(new Exception("上传图片错误！"));
        }

        String basePath = request.getServletContext().getRealPath("templates/images/");
        System.out.println("图片保存的具体路径为:" + basePath);
        File directory = new File(basePath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            image.transferTo(new File(basePath + image.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName = image.getOriginalFilename();
        String contentType = image.getContentType();
        logger.info("上传图片:name={},type={}", fileName, contentType);

        return new R<>("上传图片成功！");
    }


    /**
     * 这里稍作修改就可以了
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/app/img/upload")
    public R<String> picture (HttpServletRequest request, HttpServletResponse response) {
        //创建一个临时文件存放要上传的文件，第一个参数为上传文件大小，第二个参数为存放的临时目录
        DiskFileItemFactory factory = new DiskFileItemFactory(1024*1024*5,new File("D:\\temp1"));
        // 设置缓冲区大小为 5M
        factory.setSizeThreshold(1024 * 1024 * 5);
        // 创建一个文件上传的句柄
        ServletFileUpload upload = new ServletFileUpload(factory);

        //设置上传文件的整个大小和上传的单个文件大小
        upload.setSizeMax(1024*1024*50);
        upload.setFileSizeMax(1024*1024*5);
        String[] fileExts = {"doc","zip","rar","jpg","txt"};



        return null;
    }




}