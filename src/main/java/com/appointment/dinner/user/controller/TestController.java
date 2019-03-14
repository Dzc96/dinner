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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Tony
 * @Time 2019/1/15
 */
@RestController
@RequestMapping("/user")
public class TestController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     * /user/{userId}   HTTP1.1 8 get  post  delete  put
     */
    @Autowired
    private TestService testService;

    @GetMapping("/v1/{userId}")
    public R<SysUser> getUserById(@PathVariable("userId") Long userId) {
        return new R<>(testService.getUserById(userId));
    }


    @GetMapping("/v1/list")
    public R<List<SysUser>> list() {
        return new R<>(testService.getUserList());
    }

    @GetMapping("/v1/pages")
    public R<PageVO<SysUser>> pages(@RequestParam(value = "offset", defaultValue = "1") int offset,
                                    @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return new R<>(new PageVO<>(testService.getUserPages(offset, limit)));
    }

    @PutMapping("/v1")
    public R<String> updateUser(@Valid @RequestBody SysUser sysUser) {
        int i = testService.updateUser(sysUser);
        if (i > 0) {
            return new R<>("更新成功");
        } else {
            return new R<>(new LogicalVerificationException("更新失败"));
        }
    }

    @DeleteMapping("/v1/{userId}")
    public R<String> deleteUser(@PathVariable("userId") Long userId) {
        int i = testService.deleteUserById(userId);
        if (i > 0) {
            return new R<>("删除成功");
        } else {
            return new R<>(new LogicalVerificationException("删除失败"));
        }
    }

    /**
     * 这是处理非json参数的方法，这里的参数属于http的body部分，即请求报文的请求体，不是url后面的参数
     *
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
        SysUser user = testService.getUserByUser(sysUser);
        if (user != null) {
            return new R<>("登陆成功");
        } else {
            return new R<>(new LogicalVerificationException("登陆失败"));
        }
    }


    /**
     * 这是处理json参数的方法，这里的参数依然是http请求报文的请求体部分body
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/app/verifyMsgCode222")
    public R<String> login222(@RequestBody SysUser sysUser) {
        SysUser user = testService.getUserByUser(sysUser);
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
        SysUser user = testService.getUserByUser(sysUser);
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
    public R<String> verifyMsgCheck(@RequestParam String phoneNumber, @RequestParam String verification) {
        if (phoneNumber != null & verification != null) {
            return new R<>("短信验证成功");
        }
        return new R<>("短信验证失败");
    }

    /**
     * 把MultipartFile转成File进行保存
     *
     * @param image
     * @param request
     * @return
     */
    @PostMapping("/app/img/upload111")
    public R<String> picture111(@RequestParam MultipartFile image, HttpServletRequest request) {
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


    @PostMapping("/app/img/upload")
    public R<String> uploadImg(@RequestParam(value = "fuck0") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

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
            }

            file.transferTo(dir);//MultipartFile的内置方法transferTo()

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new R<>(e);
        }

        return new R<String>("图片上传成功！fuck you!");
    }


    /**
     * 测试多图片上传
     *
     * @return
     */
    @PostMapping("/app/img/uploadmany111")
    public R<String> uploadImgmany111(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fuck0");
//        logger.info("-----------------------");
//        logger.info("这是files数组的长度:" + files.size());
//        logger.info("-----------------------");
        for (int i = 0; i < files.size(); i++) {
//            logger.info("-----------------------");
//            logger.info("这是第" + i + "张照片");
//            logger.info("-----------------------");
            MultipartFile file = files.get(i);
            if (!file.isEmpty()) {
                try {
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
                    }
                    file.transferTo(dir);//MultipartFile的内置方法transferTo()

//                    stream = new BufferedOutputStream(new FileOutputStream(new File(path, file.getOriginalFilename())));
//                    stream.write(bytes);
//                    stream.close();
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    return new R<>(e);
                }
            }
        }

        return new R<String>("图片上传成功！fuck you!");
    }



    @PostMapping("/app/img/uploadmany222")
    public R<String> uploadImgmany222(HttpServletRequest request) {
        MultiValueMap<String, MultipartFile> multiRequest  = ((MultipartHttpServletRequest) request).getMultiFileMap();
        List<MultipartFile> fileSet = new LinkedList<>();
        for (Map.Entry<String, List<MultipartFile>> temp : multiRequest.entrySet()) {
            fileSet = temp.getValue();
        }

        //temp拿到的应该就是每一个文件
        for(MultipartFile temp : fileSet){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(new Date());
            String path = "/var/uploaded_files/" + date + "/";
            UUID uuid = UUID.randomUUID();
            String originalFilename = temp.getOriginalFilename();//------API
            String extendName = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            String fileName = uuid.toString() + extendName;
            //创建一个文件
            File dir = new File(path, fileName);
            //创建文件路径
            File filepath = new File(path);
            if (!filepath.exists()) {
                filepath.mkdirs();
            }
            try {
                temp.transferTo(dir);//MultipartFile的内置方法transferTo()
            } catch (Exception e) {
                logger.info(e.getMessage());
                return new R<>(e);
            }
        }

        return new R<String>("图片上传成功！fuck you!");
    }



    @PostMapping("/app/img/uploadmany")
    public R<String> uploadImgmany(MultipartFile[] files, HttpServletRequest request) {
       logger.info("文件的个数是：" + files.length + "个");
       if (files.length > 0) {
            for (MultipartFile file : files) {
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
                }
                try {
                    file.transferTo(dir);//MultipartFile的内置方法transferTo()
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    return new R<>(e);
                }
            }
            return new R<>("上传图片成功！！！！！！！！！！！！！！！！！！！！！！！！");
       }

        return new R<String>(new Exception("上传失败fuckyou!!!!!!!!"));
    }



}
