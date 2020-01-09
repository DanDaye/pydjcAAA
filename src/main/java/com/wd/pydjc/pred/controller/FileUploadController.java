package com.wd.pydjc.pred.controller;

import com.alibaba.fastjson.JSONArray;
import com.wd.pydjc.common.utils.UserUtil;
import com.wd.pydjc.pred.model.PredStatus;
import com.wd.pydjc.pred.service.ChatWithPython;
import com.wd.pydjc.pred.service.PredStatusServices;
import com.wd.pydjc.sys.constantString.ConstantString;
import com.wd.pydjc.sys.model.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    @Autowired
    ChatWithPython pythonChat;
    @Autowired
    PredStatusServices predStatusServices;

    /**
     * 获取分析状态
     * @return
     */
    @GetMapping("/getFeatureAnalysisStatus")
    public int getFeatureAnalysisStatus(){
        System.out.println("getStatus");
        User user = UserUtil.getCurrentUser();
        PredStatus predStatus = predStatusServices.getPredStatus(user.getSalt());
        System.out.println("featureAnalysis status:"+predStatus.getFeatureAnalysis());
        return predStatus.getFeatureAnalysis();
    }
    /**
     * 上传文件
     * @param srcFile 文件来源
     * @return 上传结果
     */
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
//            File destFile = new File(ResourceUtils.getURL("classpath:").getPath());
            File destFile = new File(ConstantString.UPLOADDIR);
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
            File upload = new File(destFile.getAbsolutePath(), user.getUsername()+"/"+ConstantString.CSV);
            //若目标文件夹不存在，则创建
            if(!upload.exists()) {
                upload.mkdirs();
            }

            //根据srcFile大小，准备一个字节数组
            byte[] bytes = srcFile.getBytes();
//            //拼接上传路径
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 获得文件原始名称
            String fileName = srcFile.getOriginalFilename();
            // 获得文件后缀名称
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//            // 生成最新的uuid文件名称
            String newFileName = times + "."+ suffixName;

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

    /**
     * 获取所有上传的文件列表
     * @return 文件列表
     */
    @GetMapping("/getAllFile")
    public List<File> getAllFile(){
        User user = UserUtil.getCurrentUser();
        //获取路径下的所有文件
        List<File> fileList = new ArrayList<>();
        File destFile = new File(ConstantString.UPLOADDIR);
        File dir = new File(destFile.getAbsolutePath(), user.getUsername()+"/"+ConstantString.CSV);
        if (dir == null){
            return null;
        }
        File[] files = dir.listFiles();
        System.out.println(files.length);
        if (files!= null){
            for (int i=0;i<files.length;i++){
                String fileName = files[i].getName();
                if (files[i].isDirectory()){
                    System.out.println("it's directory");
                }else{
                    String strFileName = files[i].getAbsolutePath();
                    System.out.println(strFileName);
                    fileList.add(files[i]);
                }
            }
        }
        return fileList;
    }
    @GetMapping("/getNewFile")
    public String getNewFile(){
        User user = UserUtil.getCurrentUser();
        //获取路径下的所有文件
        File destFile = new File(ConstantString.UPLOADDIR);
        File dir = new File(destFile.getAbsolutePath(), user.getUsername()+"/"+ConstantString.CSV);
        Long modifyTime = (long)0;
        String filePath = "";
        if (dir == null){
            return filePath;
        }
        File[] files = dir.listFiles();

        if (files!= null){
            for (File file:files){
                if (file.lastModified()>modifyTime){
                    filePath = file.getAbsolutePath();
                }
            }
        }
        System.out.println(filePath);
        return filePath;
    }

    @PostMapping("/startAnalysis")
    public int analysisStart(@RequestParam("analysisFile") String file,@RequestParam("location") String location) {
        String[] output = file.split("/");
        int len = output.length-1;
        System.out.println(output[len]);
        User user = UserUtil.getCurrentUser();
        String outputPath =  ConstantString.UPLOADDIR+"/"+user.getUsername()+ConstantString.DEALDATA+(output[len]);
        //生成outputPath和photopath
        File file1 = new File(ConstantString.UPLOADDIR+"/"+user.getUsername()+ConstantString.DEALDATA);
        if (!file1.exists()){
            file1.mkdir();
        }
        File file2 = new File(ConstantString.UPLOADDIR+"/"+user.getUsername()+"/photo");
        if (!file2.exists()){
            file2.mkdir();
        }

        System.out.println(outputPath);
        String [] arguments = new String[]{ConstantString.PYTHONDIR,ConstantString.PYTHONFOLDER+"/dealData.py",file,location,outputPath};
        //修改分析状态
        PredStatus predStatus = predStatusServices.getPredStatus(user.getSalt());
        int nowStatus = predStatus.getFeatureAnalysis();
        predStatus.setFeatureAnalysis(1);
        predStatusServices.updatePredStatus( predStatus);
        //开始分析
        //预处理数据
        int result = pythonChat.chatWith(arguments);
        //绘制图表
        String [] arguments1 = new String[]{ConstantString.PYTHONDIR,ConstantString.PYTHONFOLDER+"/dataPaint.py",outputPath};
        int result2 = pythonChat.chatWith(arguments1);
        result +=result2;
        if (result == 0){
            predStatus.setFeatureAnalysis(2);
            predStatusServices.updatePredStatus(predStatus);
        }else {
            predStatus.setFeatureAnalysis(nowStatus);
            predStatusServices.updatePredStatus(predStatus);
        }
        // 修改分析状态
        return result;
    }

    /**
     * 文件格式校验
     * @param srcFile 文件来源
     * @return 校验结果
     */
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
                Pattern pattern2 = Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}");
                System.out.println(lin[0]);
                boolean p2 = pattern2.matcher(lin[0]).matches();
                if ((p1&&p2)==false){
                    System.out.println("p1:"+p1+" p2:"+p2);
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

    /**
     * 获取json数据
     * @param filename
     * @return
     */
    @PostMapping("/getJson")
    public String getJson(@RequestParam("file") String filename){
        User user = UserUtil.getCurrentUser();
        String file = ConstantString.UPLOADDIR+"/"+user.getUsername()+"/photo"+filename;
        return readJsonFile(file);
    }
    public static String readJsonFile(String filename){
        String jsonStr = "";
        try{
            File jsonFile = new File(filename);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
