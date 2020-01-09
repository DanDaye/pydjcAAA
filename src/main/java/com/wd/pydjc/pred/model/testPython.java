package com.wd.pydjc.pred.model;

import com.wd.pydjc.pred.service.ChatWithPython;
import com.wd.pydjc.sys.constantString.ConstantString;

import java.util.regex.Pattern;

public class testPython {
    public static void main(String[] args){
        String outputPath ="/Users/liangjinsi/Documents/graduate/pydjc/src/main/resources/static/csvData/admin/dealData/20200108165539.csv";
        ChatWithPython pythonChat = new ChatWithPython();
        String pythonpath = ConstantString.PYTHONFOLDER+"/dataPaint.py";
        System.out.println("pythonpath:"+pythonpath);
        System.out.println("outputPath:"+outputPath);
        String [] arguments1 = new String[]{ConstantString.PYTHONDIR,pythonpath,outputPath};
        int result2 = pythonChat.chatWith(arguments1);
        System.out.println(result2);
    }
}
