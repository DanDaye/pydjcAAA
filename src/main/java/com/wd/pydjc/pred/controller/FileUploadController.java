package com.wd.pydjc.pred.controller;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wd.pydjc.common.utils.UserUtil;
import com.wd.pydjc.sys.model.User;
import io.swagger.annotations.Api;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 电力负荷预测
 * 文件上传
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/file")
public class FileUploadController {
//    @PostMapping("/upload")
    @PostMapping("/upload")
    public String value(@RequestParam("file") MultipartFile srcFile) {

        //查看当前登录用户信息
        User user = UserUtil.getCurrentUser();
        System.out.println(user.getUsername());

        //前端没有选择文件，srcFile为空
        if(srcFile.isEmpty()) {
            return "empty";
        }
        //选择了文件，开始上传操作
        try {
            //构建上传目标路径，找到了项目的target的classes目录
            File destFile = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!destFile.exists()) {
                destFile = new File("");
            }
            //检查文件格式是否正确
            if (!checkFile(srcFile)){
                return "illegal Format";
            }

            //输出目标文件的绝对路径
            System.out.println("file path:"+destFile.getAbsolutePath());
            //拼接子路径
            SimpleDateFormat sf_ = new SimpleDateFormat("yyyyMMddHHmmss");
            String times = sf_.format(new Date());
            File upload = new File(destFile.getAbsolutePath(), "csvdata/"+user);
            //若目标文件夹不存在，则创建
            if(!upload.exists()) {
                upload.mkdirs();
            }

            //根据srcFile大小，准备一个字节数组
            byte[] bytes = srcFile.getBytes();
            //拼接上传路径
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 获得文件原始名称
            String fileName = srcFile.getOriginalFilename();
            // 获得文件后缀名称
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            // 生成最新的uuid文件名称
            String newFileName = uuid + "."+ suffixName;

            Path path = Paths.get(upload.getAbsolutePath()+"/"+newFileName);
            //** 开始将源文件写入目标地址
            Files.write(path, bytes);
            System.out.println("完整的上传路径："+path);
        } catch (IOException e) {
            e.printStackTrace();
            return "error:"+e;
        }
        return "success";
    }

    public boolean checkFile(MultipartFile srcFile){
        InputStreamReader isr = null;
        BufferedReader br =  null;
        try{
            isr = new InputStreamReader(srcFile.getInputStream());
            br = new BufferedReader(isr);
            String line = null;
            List<List<String>> strs = new ArrayList<>();
            // 检查标题栏是否正确
            if ((line = br.readLine())!= null){
                if (!line.equals("date,load")){
                    return false;
                }
            }
            while ((line = br.readLine()) != null){
                String[] lin = line.split(",");
                if (lin.length != 2){
                    return false;
                }
                Pattern pattern1 = Pattern.compile("[0-9]*(\\.?)[0-9]*");
                boolean p1 = pattern1.matcher(lin[1]).matches();
                Pattern pattern2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z");
                boolean p2 = pattern2.matcher(lin[0]).matches();
                System.out.println("p1:"+p1+" p2:"+p2);
                if ((p1&&p2)==false){
                    return false;
                }
            }
            JSONArray array = (JSONArray) JSONArray.toJSON(strs);
//                System.out.println(array);
        }catch (IOException ie){
            try {
                if (br!=null){
                    br.close();
                }
                if (isr != null){
                    isr.close();
                }
            }catch (IOException ioe){

            }
        }
        return true;
    }
}
