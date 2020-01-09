package com.wd.pydjc.pred.service;

import com.wd.pydjc.sys.constantString.ConstantString;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
@Service
public class ChatWithPython {
    public int chatWith(String[] arguments){
      try{
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            String line = null;
            while ((line = in.readLine())!= null){
                System.out.println(line);
            }
            if (null != line && line.equals("error")){
                throw new Exception(line);
            }
            in.close();
            int re = process.waitFor();
            System.out.println(re);
            return re;
        }catch (Exception e){
            e.printStackTrace();
            return 3;
        }
    }
}
