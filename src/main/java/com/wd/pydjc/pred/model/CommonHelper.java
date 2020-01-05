package com.wd.pydjc.pred.model;

import java.io.*;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class CommonHelper {

    public static String msgSafeBase(String msg, String algorithmName) throws Exception {
        return msgSafeBase(msg.getBytes("UTF8"), algorithmName);
    }

    public static String msgSafeBase(byte[] data, String algorithmName) throws Exception {
        MessageDigest m = MessageDigest.getInstance(algorithmName);
        m.update(data);
        byte s[] = m.digest();
        return Hex.encodeHexString(s);
    }

    public static String msgSafeBase(InputStream inputStream, String algorithmName) throws Exception {
        MessageDigest m = MessageDigest.getInstance(algorithmName);

        //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
            m.update(buffer, 0, length);
        }
        inputStream.close();

        byte s[] = m.digest();
        return Hex.encodeHexString(s);
    }


    public static String msgSafeBaseMD5(byte[] data) throws Exception {
        return msgSafeBase(data, "MD5");
    }

    public static String msgSafeBaseMD5(InputStream inputStream) throws Exception {
        return msgSafeBase(inputStream, "MD5");
    }

    public static String msgSafeBaseSHA256(byte[] data) throws Exception {
        return msgSafeBase(data, "SHA-256");
    }

    public static String msgSafeBaseSHA256(InputStream inputStream) throws Exception {
        return msgSafeBase(inputStream, "SHA-256");
    }
}